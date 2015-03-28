package com.asteria.task;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.google.common.base.Preconditions;

/**
 * The task handler that oversees the processing of all submitted tasks. It
 * makes sure tasks are stopped when requested and executed at the correct
 * time.
 * <p>
 * <p>
 * The data structures that hold tasks for processing are not thread safe,
 * which
 * means tasks should only be submitted on the main game thread.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class TaskHandler {

    /**
     * The list that holds all of the pending tasks that are awaiting
     * execution.
     */
    private static final List<Task> PENDING_LIST = new LinkedList<>();

    /**
     * The queue that holds all of the tasks that are ready to be executed.
     */
    private static final Queue<Task> RUNNING_QUEUE = new ArrayDeque<>(50);

    /**
     * Queues pending tasks that are ready to be executed and executes tasks
     * that were previously queued.
     *
     * @throws Exception
     *         if any errors occur while processing the tasks.
     */
    public static void sequence() throws Exception {
        Iterator<Task> it = PENDING_LIST.iterator();
        while (it.hasNext()) {
            Task t = it.next();
            t.onSequence();
            if (t.needsExecute()) {
                RUNNING_QUEUE.add(t);
            }
            else if (!t.isRunning()) {
                it.remove();
            }
        }

        Task t;
        while ((t = RUNNING_QUEUE.poll()) != null) {
            try {
                t.execute();
            } catch (Throwable ex) {
                ex.printStackTrace();
                t.onThrowable(ex);
            }
        }
    }

    /**
     * Submits {@code task} to this task handler. The task must be running for
     * it to be successfully submitted.
     *
     * @param task
     *         the task to submit to this task handler.
     */
    public static void submit(Task task) {
        Preconditions.checkArgument(task.isRunning());
        task.onSubmit();
        if (task.isInstant())
            task.execute();
        if (task.isRunning())
            PENDING_LIST.add(task);
    }

    /**
     * Cancels all tasks with {@code key} as their key attachment.
     *
     * @param key
     *         the key to cancel all tasks with.
     */
    public static void cancel(Object key) {
        PENDING_LIST.stream().filter(t -> t.getKey().equals(key)).forEach(t
                -> t.cancel());
    }

    /**
     * Determines if any task with {@code key} as their key attachment is
     * currently running.
     *
     * @param key
     *         the key to determine this for.
     * @return {@code true} if there is a running task with that key attachment,
     * {@code false} otherwise.
     */
    public static boolean running(Object key) {
        return PENDING_LIST.stream().anyMatch(t -> t.getKey().equals(key) &&
                t.isRunning());
    }
}
