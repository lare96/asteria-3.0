package com.asteria.task;

/**
 * An event listener is a {@link Task} implementation that executes an
 * assignment when some sort of occurrence happens. They can be configured to
 * stop listening after the occurrence happens, or to keep listening and
 * executing the assignment accordingly. These event listeners can also be
 * configured to check for the occurrence at certain rates (which of course, can
 * be dynamically changed).
 *
 * @author lare96 <http://github.com/lare96>
 */
public abstract class EventListener extends Task {

    /**
     * Determines if the listener should shutdown after execution.
     */
    private final boolean shutdown;

    /**
     * Create a new {@link EventListener}.
     *
     * @param shutdown
     *            if the listener should shutdown after execution.
     * @param rate
     *            the rate in which the listener will listen.
     */
    public EventListener(boolean shutdown, int rate) {
        super(rate, true);
        this.shutdown = shutdown;
    }

    /**
     * Create a new {@link EventListener} that will listen at a rate of
     * {@code 1} and will shutdown after execution.
     */
    public EventListener() {
        this(true, 1);
    }

    /**
     * The listener will execute {@code run()} when invocation of this method
     * returns {@code false}.
     *
     * @return {@code true} if the code can be executed, {@code false} if the
     *         listener should keep listening.
     */
    public abstract boolean canExecute();

    /**
     * The code that will be executed when {@code canExecute()} returns
     * {@code true}.
     */
    public abstract void run();

    @Override
    public final void execute() {
        if (canExecute()) {
            try {
                run();
            } finally {
                if (shutdown)
                    this.cancel();
            }
        }
    }
}
