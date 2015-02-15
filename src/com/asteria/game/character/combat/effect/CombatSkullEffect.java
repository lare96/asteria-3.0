package com.asteria.game.character.combat.effect;

import com.asteria.game.character.Flag;
import com.asteria.game.character.player.Player;
import com.asteria.task.TaskHandler;

/**
 * The combat effect applied when a player needs to be skulled.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class CombatSkullEffect extends CombatEffect {

    /**
     * The player this effect is being applied to.
     */
    private final Player player;

    /**
     * Creates a new {@link CombatSkullEffect}.
     * 
     * @param player
     *            the player this effect is being applied to.
     */
    public CombatSkullEffect(Player player) {
        super(player, 50);
        this.player = player;
    }

    @Override
    public boolean apply() {
        if (player.getSkullTimer().get() > 0) {
            return false;
        }
        player.getSkullTimer().set(3000);
        player.setSkullIcon(0);
        player.getFlags().set(Flag.APPEARANCE);
        return true;
    }

    @Override
    public boolean removeOn() {
        if (player.getSkullTimer().get() <= 0) {
            player.setSkullIcon(-1);
            player.getFlags().set(Flag.APPEARANCE);
            return true;
        }
        return false;
    }

    @Override
    public void sequence() {
        player.getSkullTimer().decrementAndGet(50, 0);
    }

    @Override
    public void onLogin() {
        if (player.getSkullTimer().get() > 0) {
            player.setSkullIcon(0);
            TaskHandler.submit(this);
        }
    }
}
