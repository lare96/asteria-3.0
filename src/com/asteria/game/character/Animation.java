package com.asteria.game.character;

/**
 * The container class that represents an animation.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class Animation {

    /**
     * The identification for this animation.
     */
    private final int id;

    /**
     * The delay for this animation.
     */
    private final int delay;

    /**
     * The priority of this animation.
     */
    private final AnimationPriority priority;

    /**
     * Creates a new {@link Animation}.
     *
     * @param id
     *            the identification for this animation.
     * @param delay
     *            the delay for this animation.
     * @param priority
     *            the priority of this animation.
     */
    public Animation(int id, int delay, AnimationPriority priority) {
        this.id = id;
        this.delay = delay;
        this.priority = priority;
    }

    /**
     * Creates a new {@link Animation} with a delay of {@code 0}.
     *
     * @param id
     *            the identification for this animation.
     * @param delay
     *            the delay for this animation.
     * @param priority
     *            the priority of this animation.
     */
    public Animation(int id, AnimationPriority priority) {
        this(id, 0, priority);
    }

    /**
     * Creates a new {@link Animation} with a delay of {@code 0}, and a priority
     * of {@code NORMAL}.
     *
     * @param id
     *            the identification for this animation.
     */
    public Animation(int id) {
        this(id, AnimationPriority.NORMAL);
    }

    /**
     * A substitute for {@link Object#clone()} that creates another 'copy' of
     * this instance. The created copy is <i>safe</i> meaning it does not hold
     * <b>any</b> references to the original instance.
     *
     * @return a reference-free copy of this instance.
     */
    public Animation copy() {
        return new Animation(id, delay, priority);
    }

    /**
     * Gets the identification for this animation.
     *
     * @return the identification for this animation.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the delay for this animation.
     *
     * @return the delay for this animation.
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Gets the priority of this animation.
     *
     * @return the priority of this animation.
     */
    public AnimationPriority getPriority() {
        return priority;
    }
}