package com.asteria.game.character.player.minigame;

import com.asteria.game.NodeType;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;
import com.asteria.game.location.Position;
import com.asteria.game.plugin.PluginContext;

/**
 * The class that provides all of the functionality needed for very generic
 * minigames. These types of minigames can usually be ran on their own meaning
 * they aren't dependent on some sort of sequencer or task.
 *
 * @author lare96 <http://github.com/lare96>
 */
public abstract class Minigame implements PluginContext {

    /**
     * The current name of this minigame.
     */
    private final String name;

    /**
     * The current type of this minigame.
     */
    private final MinigameType type;

    /**
     * Creates a new {@link Minigame}.
     *
     * @param name
     *            the current name of this minigame.
     * @param type
     *            the current type of this minigame;
     */
    public Minigame(String name, MinigameType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Minigame))
            return false;
        Minigame other = (Minigame) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    /**
     * The method executed when the first click npc message is sent.
     *
     * @param player
     *            the player the message was sent for.
     * @param npc
     *            the npc that was clicked by the player.
     */
    public void onFirstClickNpc(Player player, Npc npc) {

    }

    /**
     * The method executed when the second click npc message is sent.
     *
     * @param player
     *            the player the message was sent for.
     * @param npc
     *            the npc that was clicked by the player.
     */
    public void onSecondClickNpc(Player player, Npc npc) {

    }

    /**
     * The method executed when the first click object message is sent.
     *
     * @param player
     *            the player the message was sent for.
     * @param id
     *            the identification of the object that was clicked.
     * @param position
     *            the position that the clicked object is on.
     */
    public void onFirstClickObject(Player player, int id, Position position) {

    }

    /**
     * The method executed when the second click object message is sent.
     *
     * @param player
     *            the player the message was sent for.
     * @param id
     *            the identification of the object that was clicked.
     * @param position
     *            the position that the clicked object is on.
     */
    public void onSecondClickObject(Player player, int id, Position position) {

    }

    /**
     * The method executed when the third click object message is sent.
     *
     * @param player
     *            the player the message was sent for.
     * @param id
     *            the identification of the object that was clicked.
     * @param position
     *            the position that the clicked object is on.
     */
    public void onThirdClickObject(Player player, int id, Position position) {

    }

    /**
     * The method executed when {@code player} kills {@code other}.
     *
     * @param player
     *            the player that killed another character.
     * @param other
     *            the character that was killed by the player.
     */
    public void onKill(Player player, CharacterNode other) {

    }

    /**
     * The method executed when {@code player} dies.
     *
     * @param player
     *            the player that died within the minigame.
     */
    public void onDeath(Player player) {

    }

    /**
     * Determines if {@code player} can equip {@code item} to
     * {@code equipmentSlot}.
     *
     * @param player
     *            the player attempting to equip the item.
     * @param item
     *            the item that is being equipped.
     * @param equipmentSlot
     *            the index that the item is being equipped to.
     * @return {@code true} if the player can equip the item, {@code false}
     *         otherwise.
     */
    public boolean canEquip(Player player, Item item, int equipmentSlot) {
        return true;
    }

    /**
     * Determines if {@code player} can unequip {@code item} from
     * {@code equipmentSlot}.
     *
     * @param player
     *            the player attempting to unequip the item.
     * @param item
     *            the item that is being unequipped.
     * @param equipmentSlot
     *            the index that the item is being unequipped from.
     * @return {@code true} if the player can equip the item, {@code false}
     *         otherwise.
     */
    public boolean canUnequip(Player player, Item item, int equipmentSlot) {
        return true;
    }

    /**
     * Determines if {@code player} can trade {@code other}.
     *
     * @param player
     *            the player attempting to trade the other player.
     * @param other
     *            the player that is being traded with.
     * @return {@code true} if the players can trade, {@code false} otherwise.
     */
    public boolean canTrade(Player player, Player other) {
        player.getMessages().sendMessage("You cannot trade in here!");
        return false;
    }

    /**
     * Determines if {@code player} can attack {@code other}.
     *
     * @param player
     *            the player attempting to attack the character.
     * @param other
     *            the character that is being targeted.
     * @return {@code true} if the player can attack, {@code false} otherwise.
     */
    public boolean canHit(Player player, CharacterNode other) {
        return other.getType() == NodeType.NPC;
    }

    /**
     * Determines if {@code player} can log out using the logout button.
     *
     * @param player
     *            the player attempting to logout.
     * @return {@code true} if the player can logout, {@code false} otherwise.
     */
    public boolean canLogout(Player player) {
        player.getMessages().sendMessage("You cannot logout in here!");
        return false;
    }

    /**
     * Determines if {@code player} can teleport somewhere.
     *
     * @param player
     *            the player attempting to teleport.
     * @param position
     *            the destination the player is teleporting to.
     * @return {@code true} if the player can teleport, {@code false} otherwise.
     */
    public boolean canTeleport(Player player, Position position) {
        player.getMessages().sendMessage("You cannot teleport in here!");
        return false;
    }

    /**
     * Determines if {@code player} can keep their items on death.
     *
     * @param player
     *            the player attempting to keep their items.
     * @return {@code true} if the player can keep their items, {@code false}
     *         otherwise.
     */
    public boolean canKeepItems(Player player) {
        return true;
    }

    /**
     * Retrieves the position {@code player} will be moved to when they respawn.
     *
     * @param player
     *            the player who is being respawned.
     * @return the position the player will be moved to on death.
     */
    public Position deathPosition(Player player) {
        return new Position(3093, 3244);
    }

    /**
     * The method executed when {@code player} has logged in while in the
     * minigame.
     *
     * @param player
     *            the player that has logged in.
     */
    public abstract void onLogin(Player player);

    /**
     * The method executed when {@code player} has disconnected while in the
     * minigame.
     *
     * @param player
     *            the player that has logged out.
     */
    public abstract void onLogout(Player player);

    /**
     * Determines if {@code player} is in this minigame.
     *
     * @param player
     *            the player to determine this for.
     * @return {@code true} if this minigame contains the player, {@code false}
     *         otherwise.
     */
    public abstract boolean contains(Player player);

    /**
     * Gets the current name of this minigame.
     *
     * @return the name of this minigame.
     */
    public final String getName() {
        return name;
    }

    /**
     * Gets the current type of this minigame.
     *
     * @return the type of this minigame.
     */
    public final MinigameType getType() {
        return type;
    }
}
