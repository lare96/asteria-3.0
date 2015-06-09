package com.asteria.network.packet.impl;

import com.asteria.game.World;
import com.asteria.game.character.combat.magic.CombatSpell;
import com.asteria.game.character.combat.magic.CombatSpells;
import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.npc.NpcDefinition;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.location.Location;
import com.asteria.game.location.Position;
import com.asteria.game.plugin.context.NpcFirstClickPlugin;
import com.asteria.game.plugin.context.NpcSecondClickPlugin;
import com.asteria.network.ByteOrder;
import com.asteria.network.DataBuffer;
import com.asteria.network.ValueType;
import com.asteria.network.packet.PacketDecoder;

/**
 * The packet sent from the client when a player attacks or clicks on an NPC.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcActionPacket extends PacketDecoder {

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {
        if (player.isDisabled())
            return;
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
        Position position = npc.getPosition().copy();
        player.getMovementListener().append(() -> {
            if (player.getPosition().withinDistance(position, 1)) {
                player.facePosition(npc.getPosition());
                npc.facePosition(player.getPosition());
                MinigameHandler.execute(player, m -> m.onFirstClickNpc(player, npc));
                World.getPlugins().execute(player, NpcFirstClickPlugin.class, new NpcFirstClickPlugin(npc));
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
        Position position = npc.getPosition().copy();
        player.getMovementListener().append(() -> {
            if (player.getPosition().withinDistance(position, 1)) {
                player.facePosition(npc.getPosition());
                npc.facePosition(player.getPosition());
                MinigameHandler.execute(player, m -> m.onSecondClickNpc(player, npc));
                World.getPlugins().execute(player, NpcSecondClickPlugin.class, new NpcSecondClickPlugin(npc));
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
        if (!Location.inMultiCombat(player) && player.getCombatBuilder().isBeingAttacked() && !npc.equals(player.getCombatBuilder()
            .getLastAttacker())) {
            player.getEncoder().sendMessage("You are already under attack!");
            return false;
        }
        return true;
    }
}
