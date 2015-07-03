package com.asteria.game.character.combat.prayer;

import com.asteria.game.character.combat.Combat;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.task.Task;

/**
 * The {@link Task} extension that handles the draining of prayer.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatPrayerTask extends Task {

    /**
     * The player attached to this task.
     */
    private final Player player;

    /**
     * An array of counters to perform a countdown.
     */
    private final int[] countdown = new int[CombatPrayer.size()];

    /**
     * The flag that determines if this task should be cancelled.
     */
    private boolean cancel = true;

    /**
     * Creates a new {@link CombatPrayerTask}.
     *
     * @param player
     *            the player attached to this task.
     */
    public CombatPrayerTask(Player player) {
        super(1, false);
        super.attach(player);
        this.player = player;
    }

    @Override
    public void execute() {
        for (CombatPrayer prayer : CombatPrayer.PRAYERS.values()) {
            if (player.getPrayerActive().has(prayer.getMask())) {
                cancel = false;
                if (++countdown[prayer.getId()] >= ((player.getBonus()[Combat.BONUS_PRAYER] / 2) + prayer.getDrainRate())) {
                    player.getSkills()[Skills.PRAYER].decreaseLevel(1);
                    Skills.refresh(player, Skills.PRAYER);
                    countdown[prayer.getId()] = 0;
                }
            }
        }

        if (player.getSkills()[Skills.PRAYER].getLevel() < 1) {
            player.getMessages().sendMessage("You've run out of prayer points!");
            CombatPrayer.deactivateAll(player);
            cancel = true;
        }

        if (cancel) {
            this.cancel();
            return;
        }
        cancel = true;
    }
}
