package com.asteria.game;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.asteria.service.Service;
import com.asteria.service.ServiceQueue;
import com.asteria.utility.LoggerUtils;

/**
 * The {@link Service} implementation that synchronizes game logic periodically
 * at strict intervals. This service is responsible for virtually running the
 * entire game.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class GameService extends Service {

    /**
     * The logger that will print important information.
     */
    private final Logger logger = LoggerUtils.getLogger(GameService.class);

    /**
     * The service queue that will be used to run all asynchronous tasks.
     */
    private final ServiceQueue serviceQueue = new ServiceQueue(GameConstants.THREAD_TIMEOUT);

    /**
     * Creates a new {@link GameService} with a execution rate of
     * {@code CYCLE_RATE}.
     */
    public GameService() {
        super(GameConstants.CYCLE_RATE);
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>
     * This method should <b>never</b> be invoked unless by the underlying
     * {@linkplain ServiceQueue service queue context}. Illegal invocation of
     * this method will lead to serious gameplay timing issues as well as other
     * unexplainable and unpredictable issues related to gameplay.
     */
    @Override
    public void execute(ServiceQueue context) {
        try {
            World.sequence();
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "An error has occured during the main game sequence!", t);
            World.getPlayers().forEach(player -> player.save());
        }
    }

    /**
     * Submits {@code service} to the backing service queue, to be executed
     * asynchronously. Please note that the task may not be executed for some
     * time after this method returns, depending on how many tasks are currently
     * in the queue of the service queue.
     *
     * @param service
     *            the service to submit to the service queue.
     */
    public void submit(Service service) {
        serviceQueue.submit(service);
    }

    /**
     * Submits {@code service} to the backing service queue, to be executed
     * asynchronously as a {@code DIRECT} service. Please note that the task may
     * not be executed for some time after this method returns, depending on how
     * many tasks are currently in the queue of the service queue.
     *
     * @param service
     *            the service to submit to the service queue.
     */
    public void submit(Runnable service) {
        submit(new Service() {
            @Override
            public void execute(ServiceQueue context) {
                service.run();
            }
        });
    }
}