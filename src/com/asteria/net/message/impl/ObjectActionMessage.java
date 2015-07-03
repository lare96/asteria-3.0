package com.asteria.net.message.impl;

import com.asteria.Server;
import com.asteria.game.World;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.location.Position;
import com.asteria.game.plugin.context.ObjectFirstClickPlugin;
import com.asteria.game.plugin.context.ObjectSecondClickPlugin;
import com.asteria.net.ByteOrder;
import com.asteria.net.ValueType;
import com.asteria.net.message.InputMessageListener;
import com.asteria.net.message.MessageBuilder;

/**
 * The message sent from the client when a player clicks an object.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ObjectActionMessage implements InputMessageListener {

    // TODO: When cache reading is done, check position of objects.

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
        if (player.isDisabled())
            return;

        switch (opcode) {
        case 132:
            firstClick(player, payload);
            break;
        case 252:
            secondClick(player, payload);
            break;
        case 70:
            thirdClick(player, payload);
            break;
        }
    }

    /**
     * Handles the first slot object click for {@code player}.
     *
     * @param player
     *            the player to handle this for.
     * @param payload
     *            the payloadfer for reading the sent data.
     */
    private void firstClick(Player player, MessageBuilder payload) {
        int objectX = payload.getShort(true, ValueType.A, ByteOrder.LITTLE);
        int objectId = payload.getShort(false);
        int objectY = payload.getShort(false, ValueType.A);
        Position position = new Position(objectX, objectY, player.getPosition().getZ());
        int size = 1;
        if (objectId < 0 || objectX < 0 || objectY < 0)
            return;
        if (Server.DEBUG)
            player.getMessages().sendMessage("[DEBUG]: ID - " + objectId + ", " + "X - " + objectX + ", Y - " + objectY);
        player.facePosition(position);
        player.getMovementListener().append(() -> {
            if (player.getPosition().withinDistance(position, size)) {
                MinigameHandler.execute(player, m -> m.onFirstClickObject(player, objectId, position.copy()));
                World.getPlugins().execute(player, ObjectFirstClickPlugin.class, new ObjectFirstClickPlugin(objectId, position, size));
            }
        });
    }

    /**
     * Handles the second slot object click for {@code player}.
     *
     * @param player
     *            the player to handle this for.
     * @param payload
     *            the payloadfer for reading the sent data.
     */
    private void secondClick(Player player, MessageBuilder payload) {
        int objectId = payload.getShort(false, ValueType.A, ByteOrder.LITTLE);
        int objectY = payload.getShort(true, ByteOrder.LITTLE);
        int objectX = payload.getShort(false, ValueType.A);
        int size = 1;
        Position position = new Position(objectX, objectY, player.getPosition().getZ());
        if (objectId < 0 || objectX < 0 || objectY < 0)
            return;
        if (Server.DEBUG)
            player.getMessages().sendMessage("[DEBUG]: ID - " + objectId + ", " + "X - " + objectX + ", Y - " + objectY);
        player.facePosition(position);
        player.getMovementListener().append(() -> {
            if (player.getPosition().withinDistance(position, size)) {
                MinigameHandler.execute(player, m -> m.onSecondClickObject(player, objectId, position.copy()));
                World.getPlugins().execute(player, ObjectSecondClickPlugin.class, new ObjectSecondClickPlugin(objectId, position, size));
            }
        });
    }

    /**
     * Handles the third slot object click for {@code player}.
     *
     * @param player
     *            the player to handle this for.
     * @param payload
     *            the payloadfer for reading the sent data.
     */
    private void thirdClick(Player player, MessageBuilder payload) {
        int objectX = payload.getShort(true, ByteOrder.LITTLE);
        int objectY = payload.getShort(false);
        int objectId = payload.getShort(false, ValueType.A, ByteOrder.LITTLE);
        int size = 1;
        Position position = new Position(objectX, objectY, player.getPosition().getZ());
        if (objectId < 0 || objectX < 0 || objectY < 0)
            return;
        player.facePosition(position);
        player.getMovementListener().append(new Runnable() {
            @Override
            public void run() {
                if (player.getPosition().withinDistance(position, size)) {
                    MinigameHandler.execute(player, m -> m.onThirdClickObject(player, objectId, position.copy()));
                    switch (objectId) {

                    }
                }
            }
        });
    }

}
