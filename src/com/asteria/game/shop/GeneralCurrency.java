package com.asteria.game.shop;

import com.asteria.game.character.player.Player;

/**
 * The parent class of all currencies that provides basic functionality for any
 * general currency.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public interface GeneralCurrency {

    /**
     * The method executed when the currency is taken from {@code player}.
     * 
     * @param player
     *            the player the currency is taken from.
     * @param amount
     *            the amount of currency that is taken.
     */
    public void takeCurrency(Player player, int amount);

    /**
     * The method executed when the currency is given to {@code player}.
     * 
     * @param player
     *            the player the currency is given to.
     * @param amount
     *            the amount of currency that is given.
     */
    public void recieveCurrency(Player player, int amount);

    /**
     * The method that retrieves the amount of currency {@code player} currently
     * has.
     * 
     * @param player
     *            the player who's currency amount will be determined.
     * @return the amount of the currency the player has.
     */
    public int currencyAmount(Player player);

    /**
     * Determines if the currency can be received when {@code player}'s
     * inventory is full.
     * 
     * @param player
     *            the player to determine this for.
     * @return {@code true} if the currency can be recieved, {@code false}
     *         otherwise.
     */
    public boolean canRecieveCurrency(Player player);
}
