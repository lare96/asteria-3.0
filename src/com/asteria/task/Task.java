package com.asteria.task;

import java.util.Objects;

import com.google.common.base.Preconditions;

/**
 * An assignment that has been scheduled to be completed sometime in the future.
 * These tasks run in intervals of {@code 600}ms and are executed on the main
 * game thread. They support dynamic delay changes, which means that the delay
 * for the task can be changed during the execution of the task. To conclude,
 * tasks have the ability to have any Object attached to them which can later be
 * utilized for things like stopping the task.
 * <p>
 * <p>
 * The data structures that hold tasks are not thread safe, which means tasks
 * should not be used across multiple threads. Tasks should only ever be used to
 * execute game logic.
 *
 * @author lare96 <http://github.com/lare96>
 */
public abstract class Task {

    /**
     * The default attachment key for all tasks.
     */
    public static final Object DEFAULT_KEY = new Object();

    /**
     * The delay for this task.
     */
    private int delay;

    /**
     * The pause delay for this task.
     */
    private int pauseDelay;

    /**
     * The counter used for determining when this task executes.
     */
    private int counter;

    /**
     * Determines if this task executes when submitted.
     */
    private final boolean instant;

    /**
     * The attachment key that will be used by this task.
     */
    private Object key;

    /**
     * Determines if this task is running.
     */
    private boolean running;

    /**
     * Creates a new {@link Task}.
     *
     * @param delay
     *            the delay for this task.
     * @param instant
     *            if this task executes when submitted.
     */
    public Task(int delay, boolean instant) {
        Preconditions.checkArgument(delay >= 0);
        this.delay = delay;
        this.instant = instant;
        this.running = true;
        attach(DEFAULT_KEY);
    }

    /**
     * The code that will be executed by this task. This should rarely; if ever,
     * be invoked unless by a {@linkplain TaskQueue task queue}. Illegal
     * invocation of this method will lead to unpredictable issues depending on
     * the contents of the task.
     */
    public abstract void execute();

    /**
     * The method executed when this task is submitted to the task manager.
     */
    public void onSubmit() {

    }

    /**
     * The method executed every {@code 600}ms when this task is sequenced.
     */
    public void onSequence() {

    }

    /**
     * The method executed when this task is cancelled using {@code cancel()}.
     */
    public void onCancel() {

    }

    /**
     * The method executed when {@code execute()} throws an error.
     *
     * @param t
     *            the error thrown by execution of the task.
     */
    public void onThrowable(Throwable t) {

    }

    /**
     * Determines if this task needs to be executed.
     *
     * @return {@code true} if this task needs to be executed, {@code false}
     *         otherwise.
     */
    public final boolean needsExecute() {
        if (pauseDelay > 0) {
            pauseDelay--;
            if (pauseDelay != 0)
                return false;
        }
        if (++counter >= delay && running) {
            counter = 0;
            return true;
        }
        return false;
    }

    /**
     * Cancels this task and executes the {@code onCancel()} method only if this
     * task is running.
     */
    public final void cancel() {
        if (running) {
            running = false;
            onCancel();
        }
    }

    /**
     * Pauses this task, or in other words temporarily cancels it for
     * {@code duration}.
     * 
     * @param duration
     *            the duration to pause this task for.
     */
    public final void pause(int duration) {
        if (pauseDelay > 0)
            throw new IllegalStateException("This task is already paused!");
        this.pauseDelay = duration;
    }

    /**
     * Attaches {@code key} to this task that can later be retrieved with
     * {@code getKey()}. This is a very useful feature because similar or
     * related tasks can be bound with the same key, and can then be retrieved
     * or cancelled later on. All player related tasks should be bound with the
     * player's instance so all tasks are automatically stopped on logout.
     * <p>
     * <p>
     * Keys with a value of {@code null} are <b>not</b> permitted, the default
     * value for all keys is {@code DEFAULT_KEY}.
     *
     * @param key
     *            the key to bind to this task, cannot be {@code null}.
     * @return an instance of this task.
     */
    public final Task attach(Object key) {
        this.key = Objects.requireNonNull(key);
        return this;
    }

    /**
     * Sets the new delay for this task in a dynamic fashion.
     *
     * @param delay
     *            the new delay to set for this task.
     */
    public final void newDelay(int delay) {
        Preconditions.checkArgument(delay >= 0);
        this.delay = delay;
    }

    /**
     * Determines if this task executes when submitted.
     *
     * @return {@code true} if this task executes when submitted, {@code false}
     *         otherwise.
     */
    public final boolean isInstant() {
        return instant;
    }

    /**
     * Gets the attachment key that will be used by this task.
     *
     * @return the attachment key.
     */
    public final Object getKey() {
        return key;
    }

    /**
     * Determines if this task is running.
     *
     * @return {@code true} if this task is running, {@code false} otherwise.
     */
    public final boolean isRunning() {
        return running;
    }
}