package com.asteria.game.character;

/**
 * The container class that represents a hit.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public class Hit {

    /**
     * The amount of damage within this hit.
     */
    private int damage;

    /**
     * The hit type represented by this hit.
     */
    private HitType type;

    /**
     * Creates a new {@link Hit}.
     * 
     * @param damage
     *            the amount of damage within this hit.
     * @param type
     *            the hit type represented by this hit.
     */
    public Hit(int damage, HitType type) {
        this.damage = damage;
        this.type = type;
        this.modify();
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
     * Modifies the {@code damage} and {@code type} fields so the hits will look
     * appropriate when displayed (things like not hitting a 15 with a blue
     * hitmark or 0 with a red hitmark).
     */
    private void modify() {
        if (this.damage == 0 && this.type == HitType.NORMAL) {
            this.type = HitType.BLOCKED;
        } else if (this.damage > 0 && this.type == HitType.BLOCKED) {
            this.damage = 0;
        } else if (this.damage < 0) {
            this.damage = 0;
        }
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
     * Sets the value for {@link Hit#damage}.
     * 
     * @param type
     *            the new value to set.
     */
    public void setDamage(int damage) {
        this.damage = damage;
        this.modify();
    }

    /**
     * Gets the hit type represented by this hit.
     * 
     * @return the hit type represented by this hit.
     */
    public HitType getType() {
        return type;
    }

    /**
     * Sets the value for {@link Hit#type}.
     * 
     * @param type
     *            the new value to set.
     */
    public void setType(HitType type) {
        this.type = type;
        this.modify();
    }
}
