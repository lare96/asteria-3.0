package com.asteria.game.character;

/**
 * The container class that represents an animation.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public class Animation {

    /**
     * The identification for this animation.
     */
    private int id;

    /**
     * The delay for this animation.
     */
    private int delay;

    /**
     * Creates a new {@link Animation}.
     * 
     * @param id
     *            the identification for this animation.
     * @param delay
     *            the delay for this animation.
     */
    public Animation(int id, int delay) {
        this.id = id;
        this.delay = delay;
    }

    /**
     * Creates a new {@link Animation} with a {@code delay} of {@code 0}.
     * 
     * @param id
     *            the identification for this animation.
     */
    public Animation(int id) {
        this(id, 0);
    }

    /**
     * A substitute for {@link Object#clone()} that creates another 'copy' of
     * this instance. The created copy is <i>safe</i> meaning it does not hold
     * <b>any</b> references to the original instance.
     * 
     * @return a reference-free copy of this instance.
     */
    public Animation copy() {
        return new Animation(id, delay);
    }

    /**
     * Gets the identification for this animation.
     * 
     * @return the identification for this animation.
     */
    public final int getId() {
        return id;
    }

    /**
     * Sets the value for {@link Animation#id}.
     * 
     * @param id
     *            the new value to set.
     */
    public final void setId(int id) {
        this.id = id;
    }

    /**
     * Gets delay for this animation.
     * 
     * @return the delay for this animation.
     */
    public final int getDelay() {
        return delay;
    }

    /**
     * Sets the value for {@link Animation#delay}.
     * 
     * @param id
     *            the new value to set.
     */
    public final void setDelay(int delay) {
        this.delay = delay;
    }
}