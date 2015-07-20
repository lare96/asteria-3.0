package com.asteria.game.character.combat.prayer;

import com.asteria.game.character.combat.Combat;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.task.Task;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;


public final class CombatPrayerTask extends Task {

    private final Player player;

    private final Multiset<CombatPrayer> counter = HashMultiset.create();

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
        if (player.getPrayerActive().isEmpty()) {
            this.cancel();
            return;
        }

        for (CombatPrayer prayer : player.getPrayerActive()) {
            if (counter.add(prayer, 1) >= drainFormula(prayer)) {
                player.getSkills()[Skills.PRAYER].decreaseLevel(1);
                Skills.refresh(player, Skills.PRAYER);
                counter.setCount(prayer, 0);
                if (checkPrayer())
                    break;
            }
        }
    }

    private boolean checkPrayer() {
        if (player.getSkills()[Skills.PRAYER].getLevel() < 1) {
            player.getMessages().sendMessage("You've run out of prayer points!");
            CombatPrayer.deactivateAll(player);
            this.cancel();
            return true;
        }
        return false;
    }

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