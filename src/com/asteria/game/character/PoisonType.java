package com.asteria.game.character;

/**
 * The enumerated type whose elements represent the different levels of poison.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum PoisonType {

    /**
     * The default poison type for ranged ammunition.
     */
    DEFAULT_RANGED(2),

    /**
     * The stronger poison type for ranged ammunition.
     */
    STRONG_RANGED(3),

    /**
     * The strongest poison type for ranged ammunition.
     */
    SUPER_RANGED(4),

    /**
     * The default poison type for melee weapons.
     */
    DEFAULT_MELEE(4),

    /**
     * The stronger poison type for melee weapons.
     */
    STRONG_MELEE(5),

    /**
     * The strongest poison type for melee weapons.
     */
    SUPER_MELEE(6),

    /**
     * The default poison type for poisonous NPCs.
     */
    DEFAULT_NPC(8),

    /**
     * The stronger poison type for poisonous NPCs.
     */
    STRONG_NPC(12),

    /**
     * The strongest poison type for poisonous NPCs.
     */
    SUPER_NPC(16);

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
     * Gets the starting damage for this poison type.
     *
     * @return the starting damage.
     */
    public final int getDamage() {
        return damage;
    }
}