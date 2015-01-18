package com.asteria.task;

/**
 * A listener that listens for some sort of event to occur before executing
 * code.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public abstract class ActionListener extends Task {

    /**
     * Determines if the listener should shutdown after execution.
     */
    private final boolean shutdown;

    /**
     * Create a new {@link ActionListener}.
     * 
     * @param shutdown
     *            if the listener should shutdown after execution.
     * @param rate
     *            the rate in which the listener will listen.
     */
    public ActionListener(boolean shutdown, int rate) {
        super(rate, true);
        this.shutdown = shutdown;
    }

    /**
     * Create a new {@link ActionListener} that will listen at a rate of
     * {@code 1} and will shutdown after execution.
     */
    public ActionListener() {
        this(true, 1);
    }

    /**
     * The listener will execute {@link ActionListener#run()} when invocation of
     * this method returns {@code false}.
     * 
     * @return {@code true} if the code can be executed, {@code false} if the
     *         listener should keep listening.
     */
    public abstract boolean canExecute();

    /**
     * The code that will be executed when {@link ActionListener#canExecute()}
     * returns {@code false}.
     */
    public abstract void run();

    @Override
    public void execute() {
        if (canExecute()) {
            try {
                run();
            } finally {
                if (shutdown) {
                    this.cancel();
                }
            }
        }
    }
}
