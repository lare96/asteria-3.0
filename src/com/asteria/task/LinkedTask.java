package com.asteria.task;

import java.util.Objects;

import com.google.common.base.Preconditions;

/**
 * A single link within a {@link LinkedTaskSequence}.
 * 
 * @author lare96 <http://github.org/lare96>
 */
public abstract class LinkedTask {

    /**
     * The name of this link, for debugging.
     */
    private final String name;

    /**
     * The delay of this link after the previous link.
     */
    private final int delay;

    /**
     * Creates a new {@link LinkedTask}.
     *
     * @param name
     *            the name of this link.
     * @param delay
     *            the delay of this link.
     */
    public LinkedTask(String name, int delay) {
        Preconditions.checkArgument(delay > 0, "delay <= 0");
        this.name = Objects.requireNonNull(name);
        this.delay = delay;
    }

    /**
     * Creates a new {@link LinkedTask} with a default name of
     * {@code standard-link}.
     *
     * @param delay
     *            the delay of this link.
     */
    public LinkedTask(int delay) {
        this("standard-link", delay);
    }

    @Override
    public final String toString() {
        return "LINKED_TASK[name= " + name + ", delay= " + delay + "]";
    }

    /**
     * The code that will be executed within this link. This should rarely; if
     * ever, be invoked unless by a {@linkplain LinkedTaskSequence linked task
     * sequence}. Illegal invocation of this method will lead to unpredictable
     * issues depending on the contents of this link.
     */
    public abstract void execute();

    /**
     * Gets the name of this link.
     * 
     * @return the name of this link.
     */
    public final String getName() {
        return name;
    }

    /**
     * Gets the delay of this link.
     * 
     * @return the delay of this link.
     */
    public final int getDelay() {
        return delay;
    }
}
