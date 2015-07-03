package com.asteria.net.message.impl;

import com.asteria.Server;
import com.asteria.game.character.player.Player;
import com.asteria.game.location.Position;
import com.asteria.net.message.InputMessageListener;
import com.asteria.net.message.MessageBuilder;

/**
 * The message sent from the client when a player makes a yellow {@code X} click,
 * a red {@code X} click, or when they click the minimap.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class MovementQueueMessage implements InputMessageListener {

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
        if (player.isDisabled())
            return;

        player.faceCharacter(null);

        if (opcode == 248) {
            player.setSkillAction(false);
            player.setFollowing(false);
            player.getCombatBuilder().cooldown(false);
            size -= 14;
        }

        if (opcode == 164) {
            player.setSkillAction(false);
            player.setFollowing(false);
            player.getCombatBuilder().cooldown(false);
        } else if (opcode == 98) {

        }

        if (player.isFrozen()) {
            player.getMessages().sendMessage("You are frozen and unable to " + "move!");
            return;
        }

        if (player.getDialogueChain() != null)
            player.getDialogueChain().interrupt();

        if (player.getTradeSession().inTradeSession()) {
            player.getTradeSession().reset(false);
        }

        player.getMessages().sendCloseWindows();
        player.setOpenShop(null);

        int steps = (size - 5) / 2;
        int[][] path = new int[steps][2];
        int firstStepX = payload.getShort(com.asteria.net.ValueType.A, com.asteria.net.ByteOrder.LITTLE);

        for (int i = 0; i < steps; i++) {
            path[i][0] = payload.get();
            path[i][1] = payload.get();
        }
        int firstStepY = payload.getShort(com.asteria.net.ByteOrder.LITTLE);
        player.getMovementQueue().reset();
        player.getMovementQueue().setRunPath(payload.get(com.asteria.net.ValueType.C) == 1);
        player.getMovementQueue().addToPath(new Position(firstStepX, firstStepY));

        for (int i = 0; i < steps; i++) {
            path[i][0] += firstStepX;
            path[i][1] += firstStepY;
            player.getMovementQueue().addToPath(new Position(path[i][0], path[i][1]));
        }
        player.getMovementQueue().finish();

        if (Server.DEBUG)
            player.getMessages().sendMessage("DEBUG[walking= " + player.getPosition().getRegion() + "]");
    }
}
