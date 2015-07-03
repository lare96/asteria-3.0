package com.asteria.game.plugin.context;

import com.asteria.game.item.Item;
import com.asteria.game.plugin.PluginContext;

/**
 * The plugin context for the item on item message.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemOnItemPlugin implements PluginContext {

    /**
     * The item that was used on the other item.
     */
    private final Item itemUsed;

    /**
     * The item that the other item was used on.
     */
    private final Item itemOn;

    /**
     * Creates a new {@link ItemOnItemPlugin}.
     *
     * @param itemUsed
     *            the item that was used on the other item.
     * @param itemOn
     *            the item that the other item was used on.
     */
    public ItemOnItemPlugin(Item itemUsed, Item itemOn) {
        this.itemUsed = itemUsed;
        this.itemOn = itemOn;
    }

    /**
     * Gets the item that was used on the other item.
     *
     * @return the item that was used.
     */
    public Item getItemUsed() {
        return itemUsed;
    }

    /**
     * Gets the item that the other item was used on.
     *
     * @return the item that was used on.
     */
    public Item getItemOn() {
        return itemOn;
    }
}
