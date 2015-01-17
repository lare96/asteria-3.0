package com.asteria.game.character.combat.magic;

/**
 * The enumerated type whose elements represent the types of stats that can be
 * weakened by combat spells.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public enum CombatWeaken {
    ATTACK_LOW(0.05),
    STRENGTH_LOW(0.05),
    DEFENCE_LOW(0.05),
    DEFENCE_HIGH(0.15),
    STRENGTH_HIGH(0.15),
    ATTACK_HIGH(0.15);

    /**
     * The rate at which this element weakens.
     */
    private final double rate;

    /**
     * Creates a new {@link CombatWeaken}.
     * 
     * @param rate
     *            the rate at which this element weakens.
     */
    private CombatWeaken(double rate) {
        this.rate = rate;
    }

    /**
     * Gets the rate at which this element weakens.
     * 
     * @return the rate.
     */
    public final double getRate() {
        return rate;
    }
}
