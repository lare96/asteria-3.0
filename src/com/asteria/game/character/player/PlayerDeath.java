package com.asteria.game.character.player;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import plugin.minigames.fightcaves.FightCavesHandler;

import com.asteria.game.GameConstants;
import com.asteria.game.character.Animation;
import com.asteria.game.character.AnimationPriority;
import com.asteria.game.character.CharacterDeath;
import com.asteria.game.character.Flag;
import com.asteria.game.character.combat.prayer.CombatPrayer;
import com.asteria.game.character.player.content.WeaponInterface;
import com.asteria.game.character.player.minigame.Minigame;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.game.item.Item;
import com.asteria.game.item.ItemNode;
import com.asteria.game.item.ItemNodeManager;
import com.asteria.game.item.ItemNodeStatic;
import com.asteria.game.item.container.Equipment;
import com.asteria.game.location.Position;
import com.asteria.utility.RandomGen;
import com.google.common.collect.Ordering;

/**
 * The {@link CharacterDeath} implementation that is dedicated to managing the
 * death process for all {@link Player}s.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public final class PlayerDeath extends CharacterDeath<Player> {

    /**
     * The random generator instance that will generate random numbers.
     */
    private final RandomGen random = new RandomGen();

    /**
     * Creates a new {@link PlayerDeath}.
     *
     * @param player
     *            the player who has died and needs the death process.
     */
    public PlayerDeath(Player player) {
        super(player);
    }

    @Override
    public void preDeath(Player character) {
        character.animation(new Animation(0x900, AnimationPriority.HIGH));
        character.setSkillAction(false);
        character.getTradeSession().reset(false);
    }

    @Override
    public void death(Player character) {
        Optional<Player> killer = character.getCombatBuilder().getDamageCache().calculateKiller();
        Optional<Minigame> optional = MinigameHandler.search(character);
        killer.ifPresent(k -> k.getMessages().sendMessage(
            random.random(GameConstants.DEATH_MESSAGES).replaceAll("-victim-", character.getFormatUsername()).replaceAll("-killer-",
                k.getFormatUsername())));
        if (optional.isPresent()) {
            optional.get().onDeath(character);
            if (!optional.get().canKeepItems(character)) {
                if (character.getRights().less(Rights.ADMINISTRATOR)) {
                    calculateDropItems(character, killer);
                }
            }
            killer.ifPresent(k -> optional.get().onKill(k, character));
            character.move(optional.get().deathPosition(character));
            return;
        }
        if (character.getRights().less(Rights.ADMINISTRATOR)) {
            calculateDropItems(character, killer);
            character.move(new Position(3093, 3244));
        }
    }

    @Override
    public void postDeath(Player character) {
        character.getMessages().sendCloseWindows();
        character.getCombatBuilder().reset();
        character.getCombatBuilder().getDamageCache().clear();
        character.getTolerance().reset();
        character.getSpecialPercentage().set(100);
        character.getMessages().sendByteState(301, 0);
        character.setSpecialActivated(false);
        character.getSkullTimer().set(0);
        character.setSkullIcon(FightCavesHandler.isChampion(character) ? Player.RED_SKULL : -1);
        character.getTeleblockTimer().set(0);
        character.animation(new Animation(65535));
        WeaponInterface.execute(character, character.getEquipment().get(Equipment.WEAPON_SLOT));
        character.getMessages().sendMessage(
            character.getRights().less(Rights.ADMINISTRATOR) ? "Oh dear, you're dead!"
                : "You are unaffected by death because of your rank.");
        character.getMessages().sendWalkable(65535);
        CombatPrayer.deactivateAll(character);
        Skills.restoreAll(character);
        character.getFlags().set(Flag.APPEARANCE);
    }

    /**
     * Calculates and drops all of the items from {@code character} for
     * {@code killer}.
     *
     * @param character
     *            the character whose items are being dropped.
     * @param killer
     *            the killer who the items are being dropped for.
     */
    private void calculateDropItems(Player character, Optional<Player> killer) {
        List<Item> keep = new LinkedList<>();
        List<Item> items = new LinkedList<>();

        character.getEquipment().forEach($it -> {
            if($it.getDefinition().isTradeable()) {
                items.add($it);
            } else {
                keep.add($it);
            }
        });
        character.getInventory().forEach($it -> {
            if ($it.getDefinition().isTradeable()) {
                items.add($it);
            } else {
                keep.add($it);
            }
        });
        if (items.size() > 0) {
            character.getEquipment().clear();
            character.getInventory().clear();
            character.getEquipment().refresh();
            character.getInventory().refresh();
            int amount = character.getSkullTimer().get() > 0 ? 0 : 3;
            if (CombatPrayer.isActivated(character, CombatPrayer.PROTECT_ITEM))
                amount++;
            if (amount > 0) {
                items.sort(new Ordering<Item>() {
                    @Override
                    public int compare(Item left, Item right) {
                        return Integer.compare(left.getDefinition().getGeneralPrice(), right.getDefinition().getGeneralPrice());
                    }
                }.reverse());
                for (Iterator<Item> it = items.iterator(); it.hasNext();) {
                    Item next = it.next();
                    if (amount == 0) {
                        break;
                    }
                    character.getInventory().add(new Item(next.getId()));
                    if (next.getDefinition().isStackable() && next.getAmount() > 1) {
                        next.decrementAmountBy(1);
                    } else {
                        it.remove();
                    }
                    amount--;
                }
            }
            ItemNodeManager.register(!killer.isPresent() ? new ItemNodeStatic(new Item(526), character.getPosition()) : new ItemNode(
                new Item(526), character.getPosition(), killer.get()));
            items.stream().forEach(
                item -> ItemNodeManager.register(!killer.isPresent() ? new ItemNodeStatic(item, character.getPosition()) : new ItemNode(
                    item, character.getPosition(), killer.get())));
            character.getInventory().addAll(keep);
        } else {
            ItemNodeManager.register(!killer.isPresent() ? new ItemNodeStatic(new Item(526), character.getPosition()) : new ItemNode(
                new Item(526), character.getPosition(), killer.get()));
        }
    }
}
