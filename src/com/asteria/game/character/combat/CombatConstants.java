package com.asteria.game.character.combat;

/**
 * The class that contains a collection of constants related to combat. This
 * class serves no other purpose than to hold constants.
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class CombatConstants {

    /**
     * The amount of time it takes in seconds for cached damage to timeout.
     */
    public static final long DAMAGE_CACHE_TIMEOUT = 60;

    /**
     * The percentage at which damage is reduced by combat protection prayers.
     */
    public static final double PRAYER_DAMAGE_REDUCTION = .20;

    /**
     * The percentage at which accuracy is reduced by combat protection prayers.
     */
    public static final double PRAYER_ACCURACY_REDUCTION = .255;

    /**
     * The percentage at which hitpoints will be healed by from the prayer level
     * when using redemption.
     */
    public static final double REDEMPTION_PRAYER_HEAL = .25;

    /**
     * The maximum amount of damage that retribution can inflict.
     */
    public static final int MAXIMUM_RETRIBUTION_DAMAGE = 15;

    /**
     * The radius in which the retribution effect will take place.
     */
    public static final int RETRIBUTION_RADIUS = 5;

    /**
     * The default constructor.
     *
     * @throws UnsupportedOperationException
     *             if this class is instantiated.
     */
    private CombatConstants() {
        throw new UnsupportedOperationException("This class cannot be instantiated!");
    }
}
