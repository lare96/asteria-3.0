package com.asteria.service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * The class that manages an internal queue of {@link Service}s within a
 * {@link ScheduledExecutorService}. The primary use for this class is to make
 * executing scheduled asynchronous tasks more manageable.
 * <p>
 * <p>
 * Please keep in mind that even though the management of services is
 * synchronized, that the code within the services must be thread safe or race
 * conditions and other concurrency issues can occur.
 * 
 * @author lare96 <http://github.org/lare96>
 */
public final class ServiceQueue {

    /**
     * The executor service that will allow us to execute various
     * {@link Service}s asynchronously.
     */
    private final ScheduledExecutorService executor;

    /**
     * Creates a new {@link ServiceQueue}.
     *
     * @param executor
     *            the executor to run this service queue with.
     */
    public ServiceQueue(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    /**
     * Creates a new {@link ServiceQueue} with a timeout value of
     * {@code seconds}.
     */
    public ServiceQueue(long seconds) {
        this(createServiceExecutor(seconds));
    }

    /**
     * Creates a new {@link ServiceQueue} with a timeout value of {@code 0},
     * meaning this service queue will never go idle.
     */
    public ServiceQueue() {
        this(createServiceExecutor(0));
    }

    /**
     * Submits {@code service} to this service queue to be executed by the
     * internal executor either as a scheduled or direct service. A direct
     * service has a delay of {@code 0} and a scheduled has a delay of above
     * that.
     *
     * @param service
     *            the service to submit to this service queue.
     */
    public void submit(Service service) {
        Preconditions.checkState(!executor.isShutdown(), "Cannot submit services to a ServiceQueue that has been shutdown.");
        Preconditions.checkArgument(!service.isDone(), "Cannot submit cancelled services to this ServiceQueue.");
        if (service.getDelay() == 0) {
            submitDirect(service);
        } else {
            submitScheduled(service);
        }
    }

    /**
     * Submits a scheduled {@code service} to this service queue to be executed
     * by the internal executor.
     *
     * @param service
     *            the service to submit to this service queue.
     */
    private void submitScheduled(Service service) {
        service.setFuture(executor.scheduleAtFixedRate(() -> {
            try {
                service.execute(this);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }, service.getDelay(), service.getDelay(), TimeUnit.MILLISECONDS));
    }

    /**
     * Submits a direct {@code service} to this service queue to be executed by
     * the internal executor.
     *
     * @param service
     *            the service to submit to this service queue.
     */
    private void submitDirect(Service service) {
        service.setFuture(executor.submit(() -> {
            try {
                service.execute(this);
                service.cancel();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }));
    }

    /**
     * Attempts to gracefully terminate this service queue. After this function
     * returns, this service queue will not be able to be used.
     * <p>
     * <p>
     * Please note that this function does indeed block the underlying thread
     * until {@code 5} seconds pass or all services complete.
     */
    public void terminate() {
        try {
            executor.shutdownNow();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {}
    }

    /**
     * Creates and configures a new {@link ScheduledExecutorService} with a
     * timeout value of {@code seconds}. If the timeout value is below or equal
     * to zero then the returned executor will never timeout.
     *
     * @return the newly created and configured executor service.
     */
    private static ScheduledExecutorService createServiceExecutor(long seconds) {
        Preconditions.checkArgument(seconds >= 0, "The timeout value must be equal to or greater than 0!");
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.setRejectedExecutionHandler(new CallerRunsPolicy());
        executor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("ServiceQueueThread").build());
        if (seconds > 0) {
            executor.setKeepAliveTime(seconds, TimeUnit.SECONDS);
            executor.allowCoreThreadTimeOut(true);
        }
        return executor;
    }
}
