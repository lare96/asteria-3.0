package com.asteria.game.character.npc.drop;

import com.asteria.utility.RandomGen;

/**
 * The enumerated type whose elements represent the NPC drop rates.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum NpcDropChance {
    ALWAYS(1, 1) {
        @Override
        public boolean successful(RandomGen random) {
            return true;
        }
    },
    COMMON(2, 50),
    UNCOMMON(51, 100),
    RARE(101, 512),
    VERY_RARE(513, 1000);

    /**
     * The numerator of this NPC drop rate.
     */
    private final int numerator;

    /**
     * The denominator of this NPC drop rate.
     */
    private final int denominator;

    /**
     * Creates a new {@link NpcDropChance}.
     *
     * @param numerator
     *            the numerator of this NPC drop rate.
     * @param denominator
     *            the denominator of this NPC drop rate.
     */
    private NpcDropChance(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * Determines if an NPC drop will be successful or not.
     *
     * @param random
     *            the random number generator used to determine this.
     * @return {@code true} if the drop was successful, {@code false} otherwise.
     */
    public boolean successful(RandomGen random) {
        return (random.inclusive(numerator, denominator)) % numerator == 0;
    }

    /**
     * Gets the numerator of this NPC drop rate.
     *
     * @return the numerator.
     */
    public final int getNumerator() {
        return numerator;
    }

    /**
     * Gets the denominator of this NPC drop rate.
     *
     * @return the denominator.
     */
    public final int getDenominator() {
        return denominator;
    }
}
