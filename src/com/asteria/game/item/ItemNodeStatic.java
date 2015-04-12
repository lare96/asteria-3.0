package com.asteria.game.item;

import com.asteria.game.World;
import com.asteria.game.character.player.Player;
import com.asteria.game.location.Position;

/**
 * The node implementation that represents an item everyone can see by default
 * on the ground.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemNodeStatic extends ItemNode {

    /**
     * The current item policy of this node.
     */
    private final ItemPolicy policy;

    /**
     * The flag that determines if this item node is awaiting respawn.
     */
    private boolean needsRespawn;

    /**
     * Creates a new {@link ItemNodeStatic}.
     *
     * @param item
     *            the item concealed within this node.
     * @param position
     *            the position this node is on.
     * @param policy
     *            the current item policy of this node.
     */
    public ItemNodeStatic(Item item, Position position, ItemPolicy policy) {
        super(item, position, null);
        super.setState(ItemState.SEEN_BY_EVERYONE);
        this.policy = policy;
    }

    /**
     * Creates a new {@link ItemNodeStatic} with the default policy.
     *
     * @param item
     *            the item concealed within this node.
     * @param position
     *            the position this node is on.
     */
    public ItemNodeStatic(Item item, Position position) {
        this(item, position, ItemPolicy.TIMEOUT);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (needsRespawn ? 1231 : 1237);
        result = prime * result + ((policy == null) ? 0 : policy.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (!(obj instanceof ItemNodeStatic))
            return false;
        ItemNodeStatic other = (ItemNodeStatic) obj;
        if (needsRespawn != other.needsRespawn)
            return false;
        if (policy != other.policy)
            return false;
        return true;
    }

    @Override
    public void create() {
        World.getPlayers().forEach(p -> {
            if (p.getPosition().withinDistance(getPosition(), 60)) {
                p.getEncoder().sendGroundItem(this);
            }
        });
    }

    @Override
    public void onSequence() {
        switch (policy) {
        case TIMEOUT:
            super.setRegistered(false);
            break;
        case RESPAWN:
            if (needsRespawn) {
                dispose();
                needsRespawn = false;
                super.setState(ItemState.SEEN_BY_EVERYONE);
            }
            break;
        }
    }

    @Override
    public void onPickup(Player player) {
        switch (policy) {
        case TIMEOUT:
            ItemNodeManager.unregister(this);
            break;
        case RESPAWN:
            dispose();
            needsRespawn = true;
            super.setState(ItemState.HIDDEN);
            break;
        }
        player.getInventory().add(super.getItem());
    }
}