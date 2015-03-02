package com.asteria.network.packet.impl;

import com.asteria.game.character.player.Player;
import com.asteria.network.DataBuffer;
import com.asteria.network.packet.PacketDecoder;

/**
 * The packet sent from the client when a player adds, removes, or sends someone
 * a message.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class PrivateMessagingPacket extends PacketDecoder {

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {
        if (player.isDisabled())
            return;

        switch (opcode) {
        case 188:
            addFriend(player, buf);
            break;
        case 215:
            removeFriend(player, buf);
            break;
        case 133:
            addIgnore(player, buf);
            break;
        case 74:
            removeIgnore(player, buf);
            break;
        case 126:
            sendMessage(player, size, buf);
            break;
        }
    }

    /**
     * Handles the adding of a new friend.
     * 
     * @param player
     *            the player to handle this for.
     * @param buf
     *            the buffer used for reading sent data.
     */
    private void addFriend(Player player, DataBuffer buf) {
        long name = buf.getLong();
        if (name < 0)
            return;
        player.getPrivateMessage().addFriend(name);
    }

    /**
     * Handles the removing of an existing friend.
     * 
     * @param player
     *            the player to handle this for.
     * @param buf
     *            the buffer used for reading sent data.
     */
    private void removeFriend(Player player, DataBuffer buf) {
        long name = buf.getLong();
        if (name < 0)
            return;
        player.getPrivateMessage().removeFriend(name);
    }

    /**
     * Handles the adding of a new ignore.
     * 
     * @param player
     *            the player to handle this for.
     * @param buf
     *            the buffer used for reading sent data.
     */
    private void addIgnore(Player player, DataBuffer buf) {
        long name = buf.getLong();
        if (name < 0)
            return;
        player.getPrivateMessage().addIgnore(name);
    }

    /**
     * Handles the removing of an existing ignore.
     * 
     * @param player
     *            the player to handle this for.
     * @param buf
     *            the buffer used for reading sent data.
     */
    private void removeIgnore(Player player, DataBuffer buf) {
        long name = buf.getLong();
        if (name < 0)
            return;
        player.getPrivateMessage().removeIgnore(name);
    }

    /**
     * Handles the sending of a private message.
     * 
     * @param player
     *            the player to handle this for.
     * @param buf
     *            the buffer used for reading sent data.
     */
    private void sendMessage(Player player, int size, DataBuffer buf) {
        long to = buf.getLong();
        int newSize = size - 8;
        byte[] message = buf.getBytes(size);
        if (to < 0 || newSize < 0 || message == null)
            return;
        if (!player.getFriends().contains(to)) {
            player.getEncoder().sendMessage("You cannot send a message to a player not on your friends list!");
            return;
        }
        player.getPrivateMessage().sendPrivateMessage(to, message, newSize);
    }
}
