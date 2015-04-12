package com.asteria.game.character;

import com.asteria.utility.RandomGen;

/**
 * The enumerated type whose elements represent the different levels of poison.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum PoisonType {
    MILD(5) {
        @Override
        public boolean apply(RandomGen random) {
            return random.nextInt(9) == 0;
        }
    },
    EXTRA(7) {
        @Override
        public boolean apply(RandomGen random) {
            return random.nextInt(6) == 0;
        }
    },
    SUPER(12) {
        @Override
        public boolean apply(RandomGen random) {
            return random.nextInt(3) == 0;
        }
    };

    /**
     * The starting damage for this poison type.
     */
    private final int damage;

    /**
     * Creates a new {@link PoisonType}.
     *
     * @param damage
     *            the starting damage for this poison type.
     */
    private PoisonType(int damage) {
        this.damage = damage;
    }

    /**
     * Determines if a poison effect with this poison type will be applied. The
     * {@code random} will be used to determine this.
     *
     * @param random
     *            the random number generator that will be used.
     * @return {@code true} if the effect will be applied, {@code false}
     *         otherwise.
     */
    public abstract boolean apply(RandomGen random);

    /**
     * Gets the starting damage for this poison type.
     *
     * @return the starting damage.
     */
    public final int getDamage() {
        return damage;
    }
}