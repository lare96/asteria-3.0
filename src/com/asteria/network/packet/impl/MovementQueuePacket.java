package com.asteria.network.packet.impl;

import java.util.Arrays;

import com.asteria.game.character.player.Player;
import com.asteria.game.location.Position;
import com.asteria.network.DataBuffer;
import com.asteria.network.packet.PacketDecoder;
import com.asteria.utility.Settings;

/**
 * The packet sent from the client when a player makes a yellow {@code X} click,
 * a red {@code X} click, or when they click the minimap.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class MovementQueuePacket extends PacketDecoder {

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {
        player.faceCharacter(null);

        if (opcode == 248) {
            Arrays.fill(player.getSkillEvent(), false);
            player.setFollowing(false);
            player.getCombatBuilder().cooldown(false);
            size -= 14;
        }

        if (opcode == 164) {
            Arrays.fill(player.getSkillEvent(), false);
            player.setFollowing(false);
            player.getCombatBuilder().cooldown(false);
        } else if (opcode == 98) {

        }

        if (player.isFrozen()) {
            player.getEncoder().sendMessage("You are frozen and unable to move!");
            return;
        }

        if (player.getDialogueChain() != null)
            player.getDialogueChain().interrupt();

        if (player.getTradeSession().inTradeSession()) {
            player.getTradeSession().reset(false);
        }

        player.getEncoder().sendCloseWindows();
        player.setOpenShop(null);

        int steps = (size - 5) / 2;
        int[][] path = new int[steps][2];
        int firstStepX = buf.getShort(com.asteria.network.ValueType.A, com.asteria.network.ByteOrder.LITTLE);

        for (int i = 0; i < steps; i++) {
            path[i][0] = buf.get();
            path[i][1] = buf.get();
        }
        int firstStepY = buf.getShort(com.asteria.network.ByteOrder.LITTLE);
        player.getMovementQueue().reset();
        player.getMovementQueue().setRunPath(buf.get(com.asteria.network.ValueType.C) == 1);
        player.getMovementQueue().addToPath(new Position(firstStepX, firstStepY));

        for (int i = 0; i < steps; i++) {
            path[i][0] += firstStepX;
            path[i][1] += firstStepY;
            player.getMovementQueue().addToPath(new Position(path[i][0], path[i][1]));
        }
        player.getMovementQueue().finish();

        if (Settings.DEBUG)
            player.getEncoder().sendMessage("DEBUG[walking= " + player.getPosition().getRegion() + "]");
    }
}
