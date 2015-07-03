package com.asteria.game.character.npc.drop;

import java.util.LinkedList;
import java.util.List;

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
    VERY_UNCOMMON(77, 330),
    RARE(101, 512),
    VERY_RARE(513, 1000),
    EXTREMELY_RARE(616, 1229);

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
     * Debugs this chance {@code results} times (a minimum of 10 is
     * recommended).
     * 
     * @param results
     *            the amount of times to debug this chance.
     */
    public void debug(int results) {
        RandomGen random = new RandomGen();
        int chance = 0;
        for (int amount = 0; amount < results; amount++) {
            do {
                chance++;
            } while (!successful(random));
            System.out.println("[" + this + "] Item dropped, took " + chance + " tries!");
            chance = 0;
        }
    }

    /**
     * Debugs this chance {@code results} times (a minimum of 10 is
     * recommended).
     * 
     * @param results
     *            the amount of times to debug this chance.
     */
    public void debugAverage(int results) {
        List<Integer> chances = new LinkedList<>();
        RandomGen random = new RandomGen();
        int chance = 0;
        for (int amount = 0; amount < results; amount++) {
            do {
                chance++;
            } while (!successful(random));
            chances.add(chance);
            chance = 0;
        }
        int size = chances.size();
        int average = 0;
        for (int $it : chances) {
            average += $it;
        }
        average /= size;
        System.out.println("[" + this + "] Took an average amount of " + average + " tries!");
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
