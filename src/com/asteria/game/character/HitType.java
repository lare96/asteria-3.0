package com.asteria.game.character;

/**
 * The enumerated type whose elements represent the hit type of a {@link Hit}.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum HitType {
    BLOCKED(0),
    NORMAL(1),
    POISON(2),
    DIESEASE(3);

    /**
     * The identification for this hit type.
     */
    private final int id;

    /**
     * Create a new {@link HitType}.
     *
     * @param id
     *         the identification for this hit type.
     */
    private HitType(int id) {
        this.id = id;
    }

    /**
     * Gets the identification for this hit type.
     *
     * @return the identification for this hit type.
     */
    public final int getId() {
        return id;
    }
}