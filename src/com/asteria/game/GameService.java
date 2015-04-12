package com.asteria.game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.asteria.network.ServerHandler;
import com.asteria.task.TaskHandler;
import com.asteria.utility.LoggerUtils;
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
    private static Logger logger = LoggerUtils.getLogger(GameService.class);

    /**
     * The executor that will execute various low priority services. This
     * executor implementation will allocate a maximum of {@code 1} thread that
     * will timeout after {@code 45}s of inactivity.
     */
    private static ScheduledExecutorService logicService = GameService.createLogicService();

    @Override
    public void run() {
        try {
            TaskHandler.sequence();
            ServerHandler.sequence();
            World.sequence();
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "An error has occured during the main game sequence!", t);
            World.getPlayers().forEach(player -> player.save());
        }
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

    /**
     * Gets the executor that will execute various asynchronous low priority
     * services.
     *
     * @return the logic service executor.
     */
    public static ScheduledExecutorService getLogicService() {
        return logicService;
    }
}