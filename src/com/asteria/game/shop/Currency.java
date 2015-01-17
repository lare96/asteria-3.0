package com.asteria.game.shop;

import com.asteria.game.character.player.Player;

/**
 * The enumerated type whose elements represent all of the different currencies
 * that can be used with shops.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public enum Currency {
    COINS(new ItemCurrency(995)),
    TOKKUL(new ItemCurrency(6529)),
    CASTLE_WARS_TICKETS(new ItemCurrency(4067)),
    AGILITY_ARENA_TICKETS(new ItemCurrency(2996)),
    DONATOR_POINTS(new PointCurrency() {
        @Override
        public void takeCurrency(Player player, int amount) {
            player.getDonatorPoints().decrementAndGet(amount, 0);
        }

        @Override
        public void recieveCurrency(Player player, int amount) {
            player.getDonatorPoints().incrementAndGet(amount);
        }

        @Override
        public int currencyAmount(Player player) {
            return player.getDonatorPoints().get();
        }
    });

    /**
     * The currency that is represented by this element.
     */
    private final GeneralCurrency currency;

    /**
     * Creates a new {@link Currency}.
     * 
     * @param currency
     *            the currency that is represented by this element.
     */
    private Currency(GeneralCurrency currency) {
        this.currency = currency;
    }

    /**
     * Gets the currency that is represented by this element.
     * 
     * @return the currency that is represented.
     */
    public final GeneralCurrency getCurrency() {
        return currency;
    }
}
