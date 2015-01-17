package com.asteria.game.shop;

import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;

/**
 * The currency that provides basic functionality for all physical currencies.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public class ItemCurrency implements GeneralCurrency {

    /**
     * The item identification for this currency.
     */
    private final int id;

    /**
     * Creates a new {@link ItemCurrency}.
     * 
     * @param id
     *            the item identification for this currency.
     */
    public ItemCurrency(int id) {
        this.id = id;
    }

    @Override
    public void takeCurrency(Player player, int amount) {
        player.getInventory().remove(new Item(id, amount));
    }

    @Override
    public void recieveCurrency(Player player, int amount) {
        player.getInventory().add(new Item(id, amount));
    }

    @Override
    public int currencyAmount(Player player) {
        return player.getInventory().amount(id);
    }

    @Override
    public boolean canRecieveCurrency(Player player) {
        return player.getInventory().contains(id);
    }

    /**
     * Gets the item identification for this currency.
     * 
     * @return the item identification.
     */
    public final int getId() {
        return id;
    }
}
