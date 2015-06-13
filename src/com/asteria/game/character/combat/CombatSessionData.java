package com.asteria.game.character.combat;

import com.asteria.game.NodeType;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.Hit;
import com.asteria.game.character.player.Player;
import com.google.common.base.Preconditions;

/**
 * The container that holds data for an entire combat session attack.
 *
 * @author lare96 <http://github.com/lare96>
 */
public class CombatSessionData {

    /**
     * The attacker in this combat session.
     */
    private final CharacterNode attacker;

    /**
     * The victim in this combat session.
     */
    private final CharacterNode victim;

    /**
     * The hits that will be sent when the attacker attacks.
     */
    private final CombatHit[] hits;

    /**
     * The skills that experience will be given to.
     */
    private final int[] experience;

    /**
     * The combat type the attacker is using.
     */
    private final CombatType type;

    /**
     * The flag that determines if accuracy should be taken into account.
     */
    private final boolean checkAccuracy;

    /**
     * The flag that determines if at least one hit is accurate.
     */
    private boolean accurate;

    /**
     * Creates a new {@link CombatSessionData}.
     *
     * @param attacker
     *            the attacker in this combat session.
     * @param victim
     *            the victim in this combat session.
     * @param amount
     *            the amount of hits to calculate.
     * @param type
     *            the combat type the attacker is using.
     * @param checkAccuracy
     *            determines if accuracy should be calculated for hits.
     */
    public CombatSessionData(CharacterNode attacker, CharacterNode victim, int amount, CombatType type, boolean checkAccuracy) {
        this.attacker = attacker;
        this.victim = victim;
        this.type = type;
        this.checkAccuracy = checkAccuracy;
        this.hits = calculateHits(amount);
        this.experience = determineExperience();
    }

    /**
     * Creates a new {@link CombatSessionData} with an {@code amount} of
     * {@code 0}.
     *
     * @param attacker
     *            the attacker in this combat session.
     * @param victim
     *            the victim in this combat session.
     * @param type
     *            the combat type the attacker is using.
     * @param checkAccuracy
     *            determines if accuracy should be calculated for hits.
     */
    public CombatSessionData(CharacterNode attacker, CharacterNode victim, CombatType type, boolean checkAccuracy) {
        this(attacker, victim, 0, type, checkAccuracy);
    }

    /**
     * Calculates all of the hits that will be dealt before the attack is
     * launched.
     *
     * @param amount
     *            the amount of hits to calculate, with minimum of {@code 0} and
     *            a maximum of {@code 1}.
     * @return an array of the calculated hits.
     */
    private final CombatHit[] calculateHits(int amount) {
        Preconditions.checkArgument(amount >= 0 && amount <= 4);
        if (amount == 0) {
            accurate = calculateAccuracy();
            return new CombatHit[] {};
        }
        CombatHit[] array = new CombatHit[amount];
        for (int i = 0; i < array.length; i++) {
            array[i] = new CombatHit(Combat.calculateRandomHit(attacker, victim, type), calculateAccuracy());
            if (array[i].isAccurate())
                accurate = true;
        }
        return array;
    }

    /**
     * Determines if a hit is accurate or not. This method may return different
     * values if called multiple times.
     *
     * @return {@code true} if the hit is accurate, {@code false} otherwise.
     */
    private final boolean calculateAccuracy() {
        return checkAccuracy ? Combat.isAccurate(attacker, victim, type) : true;
    }

    /**
     * Launches all of the damage concealed within this container.
     *
     * @return the amount of damage that was dealt.
     */
    public final int attack() {
        Combat.applyPrayerEffects(this);
        int counter = 0;
        int index = 0;
        Hit[] container = new Hit[hits.length];

        for (CombatHit hit : hits) {
            if (!hit.isAccurate())
                hit.setHit(new Hit(0));
            counter += hit.getHit().getDamage();
            container[index++] = hit.getHit();
        }
        if (hits.length > 0) {
            victim.damage(container);
        }
        Combat.handleExperience(attacker.getCombatBuilder(), this, counter);
        return counter;
    }

    /**
     * Determines which skills will be given experience based on the combat
     * type.
     *
     * @return an array of skills that will be given experience for this attack.
     */
    private final int[] determineExperience() {
        return attacker.getType() == NodeType.NPC ? new int[] {} : ((Player) attacker).getFightType().getStyle().skills(type);
    }

    /**
     * The method that can be overridden to do any last second modifications to
     * the container before hits are dealt to the victim.
     *
     * @return the modified combat session data container.
     */
    public CombatSessionData preAttack() {
        return this;
    }

    /**
     * The method that can be overridden to do any post-attack effects to the
     * victim. Do <b>not</b> reset the combat builder here!
     *
     * @param counter
     *            the amount of damage this attack inflicted, always {@code 0}
     *            if the attack was inaccurate.
     */
    public void postAttack(int counter) {

    }

    /**
     * Gets the attacker in this combat session.
     *
     * @return the attacker.
     */
    public final CharacterNode getAttacker() {
        return attacker;
    }

    /**
     * Gets the victim in this combat session.
     *
     * @return the victim.
     */
    public final CharacterNode getVictim() {
        return victim;
    }

    /**
     * Determines if at least one hit in this container is accurate.
     *
     * @return {@code true} if one hit is accurate, {@code false} otherwise.
     */
    public final boolean isAccurate() {
        return accurate;
    }

    /**
     * Sets the value for {@link CombatSessionData#accurate}.
     *
     * @param accurate
     *            the new value to set.
     */
    public final void setAccurate(boolean accurate) {
        this.accurate = accurate;
    }

    /**
     * Gets the hits that will be sent when the attacker attacks.
     *
     * @return the hits that will be sent.
     */
    public final CombatHit[] getHits() {
        return hits;
    }

    /**
     * Gets the skills that experience will be given to.
     *
     * @return the skills for experience.
     */
    public final int[] getExperience() {
        return experience;
    }

    /**
     * Gets the combat type the attacker is using.
     *
     * @return the combat type.
     */
    public final CombatType getType() {
        return type;
    }

    /**
     * Determines if accuracy should be taken into account.
     *
     * @return {@code true} if accuracy should be calculated, {@code false}
     *         otherwise.
     */
    public final boolean isCheckAccuracy() {
        return checkAccuracy;
    }
}
