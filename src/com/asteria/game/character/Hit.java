package com.asteria.game.character;

/**
 * The container class that represents a hit that can be dealt on a
 * {@link CharacterNode}.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class Hit {

    /**
     * The amount of damage within this hit.
     */
    private final int damage;

    /**
     * The hit type represented by this hit.
     */
    private final HitType type;

    /**
     * Creates a new {@link Hit}.
     *
     * @param damage
     *            the amount of damage within this hit.
     * @param type
     *            the hit type represented by this hit.
     */
    public Hit(int damage, HitType type) {
        if (damage == 0 && type == HitType.NORMAL) {
            type = HitType.BLOCKED;
        } else if (damage > 0 && type == HitType.BLOCKED) {
            damage = 0;
        } else if (damage < 0) {
            damage = 0;
        }
        this.damage = damage;
        this.type = type;
    }

    /**
     * Creates a new {@link Hit} with a {@code type} of {@code NORMAL}.
     *
     * @param damage
     *            the amount of damage within this hit.
     */
    public Hit(int damage) {
        this(damage, HitType.NORMAL);
    }

    /**
     * A substitute for {@link Object#clone()} that creates another 'copy' of
     * this instance. The created copy is <i>safe</i> meaning it does not hold
     * <b>any</b> references to the original instance.
     *
     * @return a reference-free copy of this instance.
     */
    public Hit copy() {
        return new Hit(damage, type);
    }

    /**
     * Gets the amount of damage within this hit.
     *
     * @return the amount of damage within this hit.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Gets the hit type represented by this hit.
     *
     * @return the hit type represented by this hit.
     */
    public HitType getType() {
        return type;
    }
}
