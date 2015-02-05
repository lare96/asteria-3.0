package com.asteria.network.packet.impl;

import java.util.Optional;
import java.util.function.Consumer;

import com.asteria.content.skills.fishing.Fishing;
import com.asteria.content.skills.fishing.Tool;
import com.asteria.game.World;
import com.asteria.game.character.combat.magic.CombatSpell;
import com.asteria.game.character.combat.magic.CombatSpells;
import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.npc.NpcDefinition;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.dialogue.DialogueChainBuilder;
import com.asteria.game.character.player.dialogue.GiveItemDialogue;
import com.asteria.game.character.player.dialogue.NpcDialogue;
import com.asteria.game.character.player.dialogue.OptionDialogue;
import com.asteria.game.character.player.dialogue.OptionType;
import com.asteria.game.character.player.dialogue.PlayerDialogue;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.item.Item;
import com.asteria.game.location.Location;
import com.asteria.game.location.Position;
import com.asteria.game.shop.Shop;
import com.asteria.network.ByteOrder;
import com.asteria.network.DataBuffer;
import com.asteria.network.ValueType;
import com.asteria.network.packet.PacketDecoder;

/**
 * The packet sent from the client when a player attacks or clicks on an NPC.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class NpcActionPacket extends PacketDecoder {

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {
        switch (opcode) {
        case 72:
            attackOther(player, buf);
            break;
        case 131:
            attackMagic(player, buf);
            break;
        case 155:
            firstClick(player, buf);
            break;
        case 17:
            secondClick(player, buf);
            break;
        }
    }

    /**
     * Handles the melee and ranged attacks on an NPC.
     * 
     * @param player
     *            the player this will be handled for.
     * @param buf
     *            the buffer that will read the sent data.
     */
    private void attackOther(Player player, DataBuffer buf) {
        int index = buf.getShort(false, ValueType.A);
        Npc npc = World.getNpcs().get(index);
        if (npc == null || !checkAttack(player, npc))
            return;
        player.getTolerance().reset();
        player.getCombatBuilder().attack(npc);
    }

    /**
     * Handles the magic attacks on an NPC.
     * 
     * @param player
     *            the player this will be handled for.
     * @param buf
     *            the buffer that will read the sent data.
     */
    private void attackMagic(Player player, DataBuffer buf) {
        int index = buf.getShort(true, ValueType.A, ByteOrder.LITTLE);
        int spellId = buf.getShort(true, ValueType.A);
        Npc npc = World.getNpcs().get(index);
        CombatSpell spell = CombatSpells.getSpell(spellId).orElse(null).getSpell();
        if (npc == null || spell == null || !checkAttack(player, npc))
            return;
        player.setAutocastSpell(null);
        player.setAutocast(false);
        player.getEncoder().sendConfig(108, 0);
        player.setCastSpell(spell);
        player.getTolerance().reset();
        player.getCombatBuilder().attack(npc);
    }

    /**
     * Handles the first click NPC slot.
     * 
     * @param player
     *            the player this will be handled for.
     * @param buf
     *            the buffer that will read the sent data.
     */
    private void firstClick(Player player, DataBuffer buf) {
        int index = buf.getShort(true, ByteOrder.LITTLE);
        Npc npc = World.getNpcs().get(index);
        if (npc == null)
            return;
        int id = npc.getId();
        Position position = new Position(npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ());
        player.getMovementListener()
            .append(() -> {
                if (player.getPosition().withinDistance(position, 1)) {
                    player.facePosition(npc.getPosition());
                    npc.facePosition(player.getPosition());
                    MinigameHandler.execute(player, m -> m.onFirstClickNpc(player, npc));
                    switch (id) {
                    case 460:
                        // Please note that the "append" function can be
                        // chained...
                        // I just did it line by line so you can see exactly how
                        // dialogues are done. I also tried to incorporate as
                        // much
                        // of the dialogue system's features as possible.
                DialogueChainBuilder builder = player.getDialogueChain();
                        builder.append(new NpcDialogue(id, "Hello, this is the new dialogue system. It",
                            "should be easier to use."));
                        builder.append(new PlayerDialogue("Cool, I'm loving it already!"));
                        builder.append(new NpcDialogue(id, "Would you like some money?"));
                        builder.append(new OptionDialogue("Yes please!", "No thanks.") {
                            @Override
                            public Optional<Consumer<OptionType>> getOptionListener() {
                                return Optional
                                    .of(n -> {
                                        if (n == OptionType.FIRST_OPTION) {
                                            builder.append(new GiveItemDialogue(new Item(995, 1000),
                                                "You receive 1000 gold coins."));
                                            builder.advance();
                                        } else if (n == OptionType.SECOND_OPTION) {
                                            player.getEncoder().sendCloseWindows();
                                        }
                                    });
                            }
                        });
                        builder.advance();
                        break;
                    case 520:
                        Shop.SHOPS.get("General Store").openShop(player);
                        break;
                    case 233:
                    case 234:
                    case 235:
                    case 236:
                        Fishing fishing = new Fishing(player, Tool.FISHING_ROD, position);
                        fishing.start();
                        break;
                    case 309:
                    case 310:
                    case 311:
                    case 314:
                    case 315:
                    case 317:
                    case 318:
                        fishing = new Fishing(player, Tool.FLY_FISHING_ROD, position);
                        fishing.start();
                        break;
                    case 312:
                        fishing = new Fishing(player, Tool.LOBSTER_POT, position);
                        fishing.start();
                        break;
                    case 313:
                        fishing = new Fishing(player, Tool.BIG_NET, position);
                        fishing.start();
                        break;
                    case 316:
                    case 319:
                        fishing = new Fishing(player, Tool.NET, position);
                        fishing.start();
                        break;
                    }
                }
            });
    }

    /**
     * Handles the second click NPC slot.
     * 
     * @param player
     *            the player this will be handled for.
     * @param buf
     *            the buffer that will read the sent data.
     */
    private void secondClick(Player player, DataBuffer buf) {
        int index = buf.getShort(false, ValueType.A, ByteOrder.LITTLE);
        Npc npc = World.getNpcs().get(index);
        if (npc == null)
            return;
        Position position = new Position(npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ());
        player.getMovementListener().append(() -> {
            if (player.getPosition().withinDistance(position, 1)) {
                player.facePosition(npc.getPosition());
                npc.facePosition(player.getPosition());
                MinigameHandler.execute(player, m -> m.onSecondClickNpc(player, npc));
                switch (npc.getId()) {
                case 309:
                case 310:
                case 311:
                case 314:
                case 315:
                case 317:
                case 318:
                    Fishing fishing = new Fishing(player, Tool.FISHING_ROD, position);
                    fishing.start();
                    break;
                case 312:
                    fishing = new Fishing(player, Tool.HARPOON, position);
                    fishing.start();
                    break;
                case 313:
                    fishing = new Fishing(player, Tool.SHARK_HARPOON, position);
                    fishing.start();
                    break;
                case 316:
                case 319:
                    fishing = new Fishing(player, Tool.FISHING_ROD, position);
                    fishing.start();
                    break;
                }
            }
        });
    }

    /**
     * Determines if {@code player} can make an attack on {@code npc}.
     * 
     * @param player
     *            the player attempting to make an attack.
     * @param npc
     *            the npc being attacked.
     * @return {@code true} if the player can make an attack, {@code false}
     *         otherwise.
     */
    private boolean checkAttack(Player player, Npc npc) {
        if (!NpcDefinition.DEFINITIONS[npc.getId()].isAttackable())
            return false;
        if (!MinigameHandler.execute(player, true, m -> m.canHit(player, npc)))
            return false;
        if (!Location.inMultiCombat(player) && player.getCombatBuilder().isBeingAttacked() && !npc.equals(player
            .getCombatBuilder().getLastAttacker())) {
            player.getEncoder().sendMessage("You are already under attack!");
            return false;
        }
        return true;
    }
}
