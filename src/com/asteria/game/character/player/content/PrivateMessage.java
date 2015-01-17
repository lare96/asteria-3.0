package com.asteria.game.character.player.content;

import com.asteria.game.World;
import com.asteria.game.character.player.Player;
import com.asteria.utility.Counter;

/**
 * The class that contains functions for managing private messaging lists.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class PrivateMessage {

    // TODO: Test adding ignore with friend already in the list.

    /**
     * The player this private messaging list belongs to.
     */
    private final Player player;

    /**
     * The last message identifier concealed within a counter.
     */
    private final Counter lastMessage = new Counter(1);

    /**
     * Creates a new {@link PrivateMessage}.
     * 
     * @param player
     *            the player this private messaging list belongs to.
     */
    public PrivateMessage(Player player) {
        this.player = player;
    }

    /**
     * Updates the friend list for {@code player} with the online status of all
     * their friends.
     */
    public void updateThisList() {
        for (long name : player.getFriends()) {
            if (name == 0)
                continue;
            player.getEncoder().sendPrivateMessageFriend(name, World.getPlayer(name).isPresent());
        }
    }

    /**
     * Updates {@code player} friends lists with with whether they are online or
     * offline.
     * 
     * @param online
     *            the status to update the other players friends lists with.
     */
    public void updateOtherList(boolean online) {
        for (Player players : World.getPlayers()) {
            if (players == null)
                continue;
            if (players.getFriends().contains(player.getUsernameHash()))
                players.getEncoder().sendPrivateMessageFriend(player.getUsernameHash(), online);
        }
    }

    /**
     * Attempts to add {@code name} to the friends list.
     * 
     * @param name
     *            the name of the new friend to add.
     */
    public void addFriend(long name) {
        if (player.getFriends().size() >= 200) {
            player.getEncoder().sendMessage("Your friends list is full!");
            return;
        }
        if (player.getFriends().contains(name)) {
            player.getEncoder().sendMessage("They are already on your friends list!");
            return;
        }
        player.getFriends().add(name);
        player.getEncoder().sendPrivateMessageFriend(name, World.getPlayer(name).isPresent());
    }

    /**
     * Attempts to add {@code name} to the friends list.
     * 
     * @param name
     *            the name of the new ignore to add.
     */
    public void addIgnore(long name) {
        if (player.getIgnores().size() >= 100) {
            player.getEncoder().sendMessage("Your ignores list is full!");
            return;
        }
        if (player.getIgnores().contains(name)) {
            player.getEncoder().sendMessage("They are already on your ignores list!");
            return;
        }
        player.getIgnores().add(name);
    }

    /**
     * Attempts to remove {@code name} from the friends list.
     * 
     * @param name
     *            the name of the existing friend to remove.
     */
    public void removeFriend(long name) {
        if (!player.getFriends().remove(name)) {
            player.getEncoder().sendMessage("They are not on your friends list.");
        }
    }

    /**
     * Attempts to remove {@code name} from the ignores list.
     * 
     * @param name
     *            the name of the existing ignore to remove.
     */
    public void removeIgnore(long name) {
        if (!player.getIgnores().remove(name)) {
            player.getEncoder().sendMessage("They are not on your ignores list.");
        }
    }

    /**
     * Attempts to send {@code message} to the player with {@code name}.
     * 
     * @param name
     *            the player with this name to send the message to.
     * @param message
     *            the message to send to the player.
     * @param size
     *            the size of the message to send.
     */
    public void sendPrivateMessage(long name, byte[] message, int size) {
        int rights = player.getRights().getProtocolValue();
        long hash = player.getUsernameHash();
        World.getPlayer(name).ifPresent(p -> p.getEncoder().sendPrivateMessage(hash, rights, message, size));
    }

    /**
     * Gets the last message identifier concealed within a counter.
     * 
     * @return the last message identifier.
     */
    public Counter getLastMessage() {
        return lastMessage;
    }
}
