package com.asteria.network.packet.impl;

import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.location.Position;
import com.asteria.game.plugin.PluginHandler;
import com.asteria.game.plugin.context.ObjectFirstClickPlugin;
import com.asteria.game.plugin.context.ObjectSecondClickPlugin;
import com.asteria.network.ByteOrder;
import com.asteria.network.DataBuffer;
import com.asteria.network.ValueType;
import com.asteria.network.packet.PacketDecoder;
import com.asteria.utility.Settings;

/**
 * The packet sent from the client when a player clicks an object.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class ObjectActionPacket extends PacketDecoder {

    // TODO: When cache reading is done, check position of objects to
    // see if you're actually near them or not.

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {
        if (player.getViewingOrb() != null)
            return;

        switch (opcode) {
        case 132:
            firstClick(player, buf);
            break;
        case 252:
            secondClick(player, buf);
            break;
        case 70:
            thirdClick(player, buf);
            break;
        }
    }

    /**
     * Handles the first slot object click for {@code player}.
     * 
     * @param player
     *            the player to handle this for.
     * @param buf
     *            the buffer for reading the sent data.
     */
    private void firstClick(Player player, DataBuffer buf) {
        int objectX = buf.getShort(true, ValueType.A, ByteOrder.LITTLE);
        int objectId = buf.getShort(false);
        int objectY = buf.getShort(false, ValueType.A);
        Position position = new Position(objectX, objectY, player.getPosition().getZ());
        int size = 1;
        if (objectId < 0 || objectX < 0 || objectY < 0)
            return;
        if (Settings.DEBUG)
            player.getEncoder().sendMessage("[DEBUG]: ID - " + objectId + ", X - " + objectX + ", Y - " + objectY);
        player.facePosition(position);
        player.getMovementListener().append(
            () -> {
                if (player.getPosition().withinDistance(position, size)) {
                    MinigameHandler.execute(player, m -> m.onFirstClickObject(player, objectId, position.copy()));
                    PluginHandler.execute(player, ObjectFirstClickPlugin.class, new ObjectFirstClickPlugin(objectId, position,
                        size));
                }
            });
    }

    /**
     * Handles the second slot object click for {@code player}.
     * 
     * @param player
     *            the player to handle this for.
     * @param buf
     *            the buffer for reading the sent data.
     */
    private void secondClick(Player player, DataBuffer buf) {
        int objectId = buf.getShort(false, ValueType.A, ByteOrder.LITTLE);
        int objectY = buf.getShort(true, ByteOrder.LITTLE);
        int objectX = buf.getShort(false, ValueType.A);
        int size = 1;
        Position position = new Position(objectX, objectY, player.getPosition().getZ());
        if (objectId < 0 || objectX < 0 || objectY < 0)
            return;
        if (Settings.DEBUG)
            player.getEncoder().sendMessage("[DEBUG]: ID - " + objectId + ", X - " + objectX + ", Y - " + objectY);
        player.facePosition(position);
        player.getMovementListener().append(
            () -> {
                if (player.getPosition().withinDistance(position, size)) {
                    MinigameHandler.execute(player, m -> m.onSecondClickObject(player, objectId, position.copy()));
                    PluginHandler.execute(player, ObjectSecondClickPlugin.class, new ObjectSecondClickPlugin(objectId, position,
                        size));
                }
            });
    }

    /**
     * Handles the third slot object click for {@code player}.
     * 
     * @param player
     *            the player to handle this for.
     * @param buf
     *            the buffer for reading the sent data.
     */
    private void thirdClick(Player player, DataBuffer buf) {
        int objectX = buf.getShort(true, ByteOrder.LITTLE);
        int objectY = buf.getShort(false);
        int objectId = buf.getShort(false, ValueType.A, ByteOrder.LITTLE);
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
