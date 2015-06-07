package com.asteria.game.character.combat.effect;

import com.asteria.game.character.player.Player;
import com.asteria.game.task.TaskHandler;

/**
 * The combat effect applied when a player needs to be teleblocked.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatTeleblockEffect extends CombatEffect {

    /**
     * The player this effect is being applied to.
     */
    private final Player player;

    /**
     * Creates a new {@link CombatTeleblockEffect}.
     *
     * @param player
     *            the player this effect is being applied to.
     */
    public CombatTeleblockEffect(Player player) {
        super(player, 1);
        this.player = player;
    }

    @Override
    public boolean apply() {
        if (player.getTeleblockTimer().get() > 0) {
            return false;
        }
        player.getTeleblockTimer().set(3000);
        player.getEncoder().sendMessage("You have just been teleblocked!");
        return true;
    }

    @Override
    public boolean removeOn() {
        if (player.getTeleblockTimer().get() <= 0) {
            player.getEncoder().sendMessage("You feel the effects of the " + "teleblock spell go away.");
            return true;
        }
        return false;
    }

    @Override
    public void sequence() {
        player.getTeleblockTimer().decrementAndGet();
    }

    @Override
    public void onLogin() {
        if (player.getTeleblockTimer().get() > 0) {
            TaskHandler.submit(this);
        }
    }
}
