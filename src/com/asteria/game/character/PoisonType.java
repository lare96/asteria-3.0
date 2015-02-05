package com.asteria.game.character;

/**
 * The enumerated type whose elements represent the different levels of poison.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public enum PoisonType {
    MILD(5),
    EXTRA(7),
    SUPER(12);

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