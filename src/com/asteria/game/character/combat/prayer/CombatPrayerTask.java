package com.asteria.game.character.combat.prayer;

import com.asteria.game.character.combat.Combat;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.task.Task;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

/**
 * A task that periodically drains prayer points for {@link Player}s. It
 * utilizes the same formula to used on actual Runescape.
 * 
 * @author lare96 <http://github.org/lare96>
 */
public final class CombatPrayerTask extends Task {

    /**
     * The player to drain prayer points.
     */
    private final Player player;

    /**
     * A set containing the tick counts for all activated {@link CombatPrayer}s.
     */
    private final Multiset<CombatPrayer> counter = HashMultiset.create();

    /**
     * Creates a new {@link CombatPrayerTask}.
     *
     * @param player
     *            the player to drain prayer points.
     */
    public CombatPrayerTask(Player player) {
        super(1, false);
        super.attach(player);
        this.player = player;
    }

    @Override
    public void execute() {

        // Checks if the EnumSet is empty, or in other words in no prayers are
        // currently active. If so cancel the task.
        if (player.getPrayerActive().isEmpty()) {
            this.cancel();
            return;
        }

        // Iterate through all of the currently activated prayers, determine if
        // a prayer point can be drained, and last but not least if a prayer
        // point can be drained then determine if the player is out of points.
        for (CombatPrayer prayer : player.getPrayerActive()) {
            if (counter.add(prayer, 1) >= drainFormula(prayer)) {
                player.getSkills()[Skills.PRAYER].decreaseLevel(1);
                Skills.refresh(player, Skills.PRAYER);
                counter.setCount(prayer, 0);

                // Determine if the player is out of points, if they are then
                // break from the loop and cancel the task.
                if (checkPrayer())
                    break;
            }
        }
    }

    /**
     * Determines if the {@link Player}s prayer has ran out.
     * 
     * @return {@code true} if the player ran out of prayer, {@code false}
     *         otherwise.
     */
    private boolean checkPrayer() {
        if (player.getSkills()[Skills.PRAYER].getLevel() < 1) {
            player.getMessages().sendMessage("You've run out of prayer points!");
            CombatPrayer.deactivateAll(player);
            this.cancel();
            return true;
        }
        return false;
    }

    /**
     * Calculates the amount of ticks needed to drain a single prayer point when
     * using {@code prayer}. This method utilizes {@link Player}s prayer bonus
     * and the prayer drain rate.
     * 
     * @param prayer
     *            the prayer to calculate for.
     * @return the amount of ticks needed to drain a prayer point.
     */
    private int drainFormula(CombatPrayer prayer) {
        double rate = prayer.getDrainRate();
        double addFactor = 1;
        double divideFactor = 30;
        double bonus = player.getBonus()[Combat.BONUS_PRAYER];
        double tick = 600;
        double second = 1000;
        return (int) (((rate + (addFactor + bonus / divideFactor)) * second) / tick);
    }
}