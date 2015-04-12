package com.asteria.game.character.combat;

import com.asteria.game.character.Hit;

/**
 * A container that conceals the hit dealt to a victim during a combat session
 * attack.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatHit {

    /**
     * The hit that will be dealt to the victim.
     */
    private Hit hit;

    /**
     * The flag that determines whether this hit is accurate.
     */
    private boolean accurate;

    /**
     * Creates a new {@link CombatHit}.
     *
     * @param hit
     *            the hit that will be dealt to the victim.
     * @param accurate
     *            the flag that determines whether thi hit is accurate.
     */
    public CombatHit(Hit hit, boolean accurate) {
        this.hit = hit;
        this.accurate = accurate;
    }

    /**
     * Gets the hit that will be dealt to the victim.
     *
     * @return the hit that will be dealt to the victim.
     */
    public Hit getHit() {
        return hit;
    }

    /**
     * Sets the value for {@link CombatHit#hit}.
     *
     * @param hit
     *            the new value to set.
     */
    public void setHit(Hit hit) {
        this.hit = hit;
    }

    /**
     * Determines if this hit is accurate.
     *
     * @return {@code true} if this hit is accurate, {@code false} otherwise.
     */
    public boolean isAccurate() {
        return accurate;
    }

    /**
     * Sets the value for {@link CombatHit#accurate}.
     *
     * @param accurate
     *            the new value to set.
     */
    public void setAccurate(boolean accurate) {
        this.accurate = accurate;
    }
}