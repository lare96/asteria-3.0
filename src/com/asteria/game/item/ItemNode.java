package com.asteria.game.item;

import com.asteria.game.Node;
import com.asteria.game.NodeType;
import com.asteria.game.World;
import com.asteria.game.character.player.Player;
import com.asteria.game.location.Position;
import com.asteria.utility.Counter;

/**
 * The node implementation that represents an item on the ground.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public class ItemNode extends Node {

    /**
     * The item concealed within this node.
     */
    private final Item item;

    /**
     * The counter that contains the amount of ticks this node has.
     */
    private final Counter counter = new Counter();

    /**
     * The item state of this node.
     */
    private ItemState state = ItemState.SEEN_BY_OWNER;

    /**
     * The player attached to this node.
     */
    private Player player;

    /**
     * Creates new {@link ItemNode}.
     * 
     * @param item
     *            the item concealed within this node.
     * @param position
     *            the position this node is on.
     * @param player
     *            the player attached to this node.
     */
    public ItemNode(Item item, Position position, Player player) {
        super(position, NodeType.ITEM);
        this.item = item.copy();
        this.player = player;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((counter == null) ? 0 : counter.hashCode());
        result = prime * result + ((item == null) ? 0 : item.hashCode());
        result = prime * result + ((player == null) ? 0 : player.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (!(obj instanceof ItemNode))
            return false;
        ItemNode other = (ItemNode) obj;
        if (counter == null) {
            if (other.counter != null)
                return false;
        } else if (!counter.equals(other.counter))
            return false;
        if (item == null) {
            if (other.item != null)
                return false;
        } else if (!item.equals(other.item))
            return false;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        if (state != other.state)
            return false;
        return true;
    }

    @Override
    public void create() {
        player.getEncoder().sendGroundItem(this);
    }

    @Override
    public void dispose() {
        switch (state) {
        case SEEN_BY_EVERYONE:
            World.getPlayers().forEach(p -> {
                if (super.getPosition().withinDistance(p.getPosition(), 60)) {
                    p.getEncoder().sendRemoveGroundItem(this);
                }
            });
            break;
        case SEEN_BY_OWNER:
            World.getPlayer(player.getUsernameHash()).ifPresent(p -> p.getEncoder().sendRemoveGroundItem(this));
            break;
        default:
            throw new IllegalStateException("Invalid item node state!");
        }
    }

    /**
     * The method executed on every sequence by the item node manager.
     * 
     * @throws IllegalStateException
     *             if the item node is in an incorrect state.
     */
    public void onSequence() {
        switch (state) {
        case SEEN_BY_OWNER:
            World.getPlayers().forEach(p -> {
                if (super.getPosition().withinDistance(p.getPosition(), 60) && !p.equals(player)) {
                    p.getEncoder().sendGroundItem(new ItemNode(item, super.getPosition(), null));
                }
            });
            player = null;
            state = ItemState.SEEN_BY_EVERYONE;
            break;
        case SEEN_BY_EVERYONE:
            super.setRegistered(false);
            break;
        default:
            throw new IllegalStateException("Invalid item node state!");
        }
    }

    /**
     * The method executed when {@code player} attempts to pickup this item.
     * 
     * @param player
     *            the player attempting to pickup this item.
     */
    public void onPickup(Player player) {
        ItemNodeManager.unregister(this);
        player.getInventory().add(item);
    }

    /**
     * Gets the item state of this node.
     * 
     * @return the item state.
     */
    public final ItemState getState() {
        return state;
    }

    /**
     * Sets the value for {@link ItemNode#state}.
     * 
     * @param state
     *            the new value to set.
     */
    public final void setState(ItemState state) {
        this.state = state;
    }

    /**
     * Gets the player attached to this node.
     * 
     * @return the player attached.
     */
    public final Player getPlayer() {
        return player;
    }

    /**
     * Sets the value for {@link ItemNode#player}.
     * 
     * @param player
     *            the new value to set.
     */
    public final void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Gets the item concealed within this node.
     * 
     * @return the item concealed.
     */
    public final Item getItem() {
        return item;
    }

    /**
     * Gets the counter that contains the amount of ticks this node has.
     * 
     * @return the counter that contains the ticks.
     */
    public final Counter getCounter() {
        return counter;
    }
}
