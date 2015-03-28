package com.asteria.network;

import com.asteria.game.GameService;

/**
 * The event that has been selected by the selector and is awaiting execution.
 * This serves as a wrapper for the event that allows the event to either be
 * executed asynchronously or right on the underlying thread.
 *
 * @author lare96 <http://github.com/lare96>
 */
public abstract class ServerSelectionEvent {

    /**
     * Determines if this server selection event is asynchronous.
     */
    private final boolean asynchronous;

    /**
     * Creates a new {@link ServerSelectionEvent}.
     *
     * @param asynchronous
     *         determines if this server selection event is asynchronous.
     */
    public ServerSelectionEvent(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }

    /**
     * The method that determines what happens when this event is executed.
     *
     * @param key
     *         the wrapped selection key for network operations.
     * @throws Exception
     *         if any errors occur during execution.
     */
    public abstract void executeEvent(ServerSelectionKey key) throws Exception;

    /**
     * Executes this event either asynchronously on the logic service thread,
     * or
     * right on the underlying game thread.
     *
     * @param key
     *         the wrapped selection key for network operations.
     */
    protected final void execute(ServerSelectionKey key) {
        Runnable r = construct(key);
        if (asynchronous) {
            GameService.getLogicService().execute(r);
            return;
        }
        r.run();
    }

    /**
     * Constructs a runnable event containing the context that this network
     * event will be executed in.
     *
     * @param key
     *         the key that that event is being executed for.
     * @return the runnable event containing the event context.
     */
    private final Runnable construct(ServerSelectionKey key) {
        return () -> {
            try {
                executeEvent(key);
            } catch (Throwable t) {
                t.printStackTrace();
                onThrowable(t, key);
            }
        };
    }

    /**
     * The method executed if a throwable is thrown while executing this event.
     *
     * @param t
     *         the throwable that was thrown.
     * @param key
     *         the wrapped selection key for network operations.
     */
    public void onThrowable(Throwable t, ServerSelectionKey key) {

    }

    /**
     * Determines if this server selection event is asynchronous.
     *
     * @return {@code true} if this is asynchronous, {@code false} otherwise.
     */
    public final boolean isAsynchronous() {
        return asynchronous;
    }
}
