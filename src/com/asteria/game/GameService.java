package com.asteria.game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.asteria.game.task.TaskHandler;
import com.asteria.network.ServerReactor;
import com.asteria.utility.LoggerUtils;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * The main game sequencer that executes game logic every {@code 600}ms. This
 * also gives access to a single threaded {@link ScheduledExecutorService} which
 * allows for the execution of low priority asynchronous services.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public final class GameService implements Runnable {

    /**
     * The logger that will print important information.
     */
    private final Logger logger = LoggerUtils.getLogger(GameService.class);

    /**
     * The executor that will execute various low priority services. This
     * executor implementation will allocate a maximum of {@code 1} thread that
     * will timeout after {@code 45}s of inactivity.
     */
    private final ScheduledExecutorService logicService = GameService.createLogicService();

    /**
     * The sequence based reactor that will handle the various network events
     * assigned to clients as they are received.
     */
    private ServerReactor reactor;

    @Override
    public void run() {
        try {
            TaskHandler.sequence();
            reactor.sequence();
            World.sequence();
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "An error has occured during the main game sequence!", t);
            World.getPlayers().forEach(player -> player.save());
        }
    }

    /**
     * Submits {@code t} to the underlying logic service to be executed
     * asynchronously. Please note that the task may not be executed for some
     * time after this method returns, depending on how many tasks are currently
     * in the queue of the logic service.
     * 
     * @param t
     *            the task to submit to the executor.
     */
    public void submit(Runnable t) {
        logicService.execute(t);
    }

    /**
     * Submits {@code t} to the underlying logic service to be repeatedly
     * executed asynchronously with an initial delay of {@code initialDelay} and
     * subsequent delays of {@code delay} in the time unit {@code unit}. Please
     * note that the task may not be executed exactly on the requested
     * intervals, depending on how many tasks are currently in the queue of the
     * logic service.
     * 
     * @param t
     *            the task to submit to the executor.
     * @return the result of the execution of this task, primarily used to
     *         cancel execution.
     */
    public ScheduledFuture<?> submit(Runnable t, long initialDelay, long delay, TimeUnit unit) {
        return logicService.scheduleAtFixedRate(t, initialDelay, delay, unit);
    }

    /**
     * Submits {@code t} to the underlying logic service to be repeatedly
     * executed asynchronously with initial and subsequent delays of
     * {@code delay} in the time unit {@code unit}. Please note that the task
     * may not be executed for some time after this method returns, depending on
     * how many tasks are currently in the queue of the logic service.
     * 
     * @param t
     *            the task to submit to the executor.
     * @return the result of the execution of this task, primarily used to
     *         cancel execution.
     */
    public ScheduledFuture<?> submit(Runnable t, long delay, TimeUnit unit) {
        return submit(t, delay, delay, unit);
    }

    /**
     * Sets the server reactor that will be used to handle network events to
     * {@code reactor}. This method can only be used if there is currently no
     * reactor set.
     * 
     * @param reactor
     *            the reactor that will be used.
     * @throws IllegalStateException
     *             if a reactor instance is already set.
     */
    public void setServerReactor(ServerReactor reactor) {
        Preconditions.checkState(this.reactor == null, "There is already a reactor set!");
        this.reactor = reactor;
    }

    /**
     * Creates and configures a new {@link ScheduledExecutorService} aimed at
     * executing low priority services. The returned executor is
     * <b>unconfigurable</b> meaning it's configuration can no longer be
     * modified.
     *
     * @return the newly created and configured executor service.
     */
    private static ScheduledExecutorService createLogicService() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.setRejectedExecutionHandler(new CallerRunsPolicy());
        executor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("LogicServiceThread").build());
        executor.setKeepAliveTime(45, TimeUnit.SECONDS);
        executor.allowCoreThreadTimeOut(true);
        return Executors.unconfigurableScheduledExecutorService(executor);
    }
}