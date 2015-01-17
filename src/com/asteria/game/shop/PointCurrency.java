package com.asteria.game.shop;

import com.asteria.game.character.player.Player;

/**
 * The currency that provides basic functionality for all non-physical
 * currencies.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public abstract class PointCurrency implements GeneralCurrency {

    @Override
    public boolean canRecieveCurrency(Player player) {
        return true;
    }
}
