package com.asteria.game.item.container;

import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;

/**
 * The container that manages the inventory for a player.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class Inventory extends ItemContainer {

    /**
     * The player who's inventory is being managed.
     */
    private final Player player;

    /**
     * Create a new {@link Inventory}.
     * 
     * @param player
     *            the player who's inventory is being managed.
     */
    public Inventory(Player player) {
        super(28, ItemContainerPolicy.NORMAL);
        this.player = player;
    }

    /**
     * Refreshes the contents of this inventory container to the interface.
     */
    public void refresh() {
        refresh(player, 3214);
    }

    @Override
    public boolean add(Item item, int slot) {
        if (!Item.valid(item))
            return false;
        if (!contains(item.getId()) && !item.getDefinition().isStackable()) {
            if (freeSlot() == -1) {
                player.getEncoder().sendMessage("You don't have enough space in your inventory!");
                return false;
            }
        }
        boolean val = super.add(item, slot);
        refresh();
        return val;
    }

    @Override
    public boolean add(Item item) {
        return add(item, -1);
    }

    @Override
    public boolean remove(Item item, int slot) {
        boolean val = super.remove(item, slot);
        refresh();
        return val;
    }

    @Override
    public boolean remove(Item item) {
        return remove(item, -1);
    }
}