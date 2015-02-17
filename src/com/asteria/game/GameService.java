package com.asteria.game;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
import com.asteria.utility.RandomGen;
import com.asteria.utility.Settings;
import com.asteria.utility.Stopwatch;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * The most important class of this server that sequentially executes, or
 * sequences all game related code.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class GameService implements Runnable {

    /**
     * The amount of benchmarks that will be collected before a status report
     * will be printed.
     */
    private static final int BENCHMARKS = 1000;

    /**
     * The logger that will print important information.
     */
    private static Logger logger = LoggerUtils.getLogger(GameService.class);

    /**
     * The random generator instance that will generate random numbers.
     */
    private static RandomGen random = new RandomGen();

    /**
     * The stopwatch that will determine the operational latency during game
     * sequences.
     */
    private static Stopwatch timer = new Stopwatch();

    /**
     * The list of benchmarks that will be used to record the average
     * operational latency of this game sequencer.
     */
    private static List<Long> benchmarks = new LinkedList<>();

    /**
     * The executor that will execute various {@code Thread.MIN_PRIORITY}
     * services. This executor implementation will allocate a maximum of 1
     * thread that will timeout after 45 {@code SECONDS} of inactivity.
     */
    private static ScheduledExecutorService logicService = GameService.createLogicService();

    @Override
    public void run() {
        try {
            if (Settings.DEBUG)
                timer.reset();
            TaskHandler.sequence();
            ServerHandler.sequence();
            World.sequence();
            if (Settings.DEBUG)
                executeBenchmarks();
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "An error has occured during the main game sequence!", t);
            World.getPlayers().forEach(player -> player.save());
        }
    }

    /**
     * Collects the benchmark for one game sequence, and prints off a statistics
     * report when a certain amount of benchmarks have been collected.
     */
    private static void executeBenchmarks() {
        if (benchmarks.size() == BENCHMARKS) {
            long num = 0;
            Iterator<Long> iterator = benchmarks.iterator();
            while (iterator.hasNext()) {
                num += iterator.next();
                iterator.remove();
            }
            double percent = random.round(((double) (num / BENCHMARKS) / 600), 10000);
            logger.info("BENCHMARK[engine load= " + (percent * 100.0) + "%]");
        } else {
            benchmarks.add(timer.elapsedTime());
        }
    }

    /**
     * Creates and configures the {@link GameService#logicService} that will
     * execute {@code Thread.MIN_PRIORITY} services. The returned executor is
     * <b>unconfigurable</b> meaning it's configuration can no longer be
     * modified.
     * 
     * @return the newly created and configured logic service.
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
     * Gets the executor that will execute various {@code Thread.MIN_PRIORITY}
     * services.
     * 
     * @return the executor that will execute various
     *         {@code Thread.MIN_PRIORITY} services.
     */
    public static ScheduledExecutorService getLogicService() {
        return logicService;
    }
}