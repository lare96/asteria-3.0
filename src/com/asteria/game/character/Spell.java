package com.asteria.game.character;

import java.util.Arrays;
import java.util.Optional;

import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.content.MagicStaff;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.game.item.Item;

/**
 * The parent class of all magic spells that provides basic functionality for
 * generic spells.
 *
 * @author lare96 <http://github.com/lare96>
 */
public abstract class Spell {

    /**
     * Determines if {@code player} can cast this spell.
     *
     * @param player
     *            the player attempting to cast this spell.
     * @return {@code true} if the player can cast the spell, {@code false}
     *         otherwise.
     */
    public boolean canCast(Player player) {
        int level = levelRequired();
        Optional<Item[]> inventory = itemsRequired(player);
        Optional<Item[]> equipment = equipmentRequired(player);

        if (player.getSkills()[Skills.MAGIC].getLevel() < level) {
            player.getMessages().sendMessage("You need a Magic level of " + level + " to cast this spell.");
            player.getCombatBuilder().reset();
            return false;
        }

        if (equipment.isPresent()) {
            if (!player.getEquipment().containsAll(equipment.get())) {
                player.getMessages().sendMessage("You do not have the required equipment to cast this spell.");
                resetPlayerSpell(player);
                player.getCombatBuilder().reset();
                return false;
            }
        }

        if (inventory.isPresent()) {
            Item[] items = MagicStaff.suppressRunes(player, inventory.get());

            if (!player.getInventory().containsAll(items)) {
                player.getMessages().sendMessage("You do not have the required items to cast this spell.");
                resetPlayerSpell(player);
                player.getCombatBuilder().reset();
                return false;
            }
            player.getInventory().removeAll(Arrays.asList(items));
        }
        return true;
    }

    /**
     * Resets the {@code player}'s autocasting if they're currently in combat.
     *
     * @param player
     *            the player to reset.
     */
    private final void resetPlayerSpell(Player player) {
        if (player.getCombatBuilder().isAttacking() || player.getCombatBuilder().isBeingAttacked() && player.isAutocast()) {
            player.setAutocastSpell(null);
            player.setAutocast(false);
            player.getMessages().sendByteState(108, 0);
            player.setCastSpell(null);
        }
    }

    /**
     * The level required to cast this spell.
     *
     * @return the level required.
     */
    public abstract int levelRequired();

    /**
     * The base experience given when this spell is cast.
     *
     * @return the base experience.
     */
    public abstract double baseExperience();

    /**
     * The items required in the inventory to cast this spell.
     *
     * @param player
     *            the player who's inventory will be checked.
     * @return the items required to cast this spell, or an empty optional if
     *         there are no items required.
     */
    public abstract Optional<Item[]> itemsRequired(Player player);

    /**
     * The items required in the equipment to cast this spell.
     *
     * @param player
     *            the player who's equipment will be checked.
     * @return the equipment required to cast this spell, or an empty optional
     *         if there is no equipment required.
     */
    public abstract Optional<Item[]> equipmentRequired(Player player);

    /**
     * The dynamic method executed when the spell is cast.
     *
     * @param cast
     *            the character casting the spell.
     * @param castOn
     *            the character the spell is being cast on, this may be
     *            {@code null}.
     */
    public abstract void startCast(CharacterNode cast, CharacterNode castOn);
}
