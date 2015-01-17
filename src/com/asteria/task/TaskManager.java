package com.asteria.task;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.google.common.base.Preconditions;

/**
 * The sequencer that will manage and execute all tasks that run on the main
 * game thread.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class TaskManager {

    /**
     * The pending list of tasks awaiting execution.
     */
    private static final List<Task> PENDING_LIST = new LinkedList<>();

    /**
     * The queue of tasks that have waited their delays and are ready to be
     * executed.
     */
    private static final Queue<Task> RUNNING_QUEUE = new ArrayDeque<>(50);

    /**
     * The sequence method that queues pending tasks that are ready to be
     * executed and executes tasks that were previously queued.
     * 
     * @throws Exception
     *             if any errors occur during the task sequence.
     */
    public static void sequence() throws Exception {
        Iterator<Task> it = PENDING_LIST.iterator();
        while (it.hasNext()) {
            Task t = it.next();

            if (t.needsExecute()) {
                RUNNING_QUEUE.add(t);
            } else if (!t.isRunning()) {
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
     * Submits {@code task} to be ran by this task manager.
     * 
     * @param task
     *            the task to be ran.
     */
    public static void submit(Task task) {
        Preconditions.checkArgument(task.isRunning());
        if (task.isInstant())
            task.execute();
        task.onSubmit();
        PENDING_LIST.add(task);
    }

    /**
     * Cancels all tasks with {@code key} as their key attachment.
     * 
     * @param key
     *            the key to cancel all tasks with.
     */
    public static void cancel(Object key) {
        PENDING_LIST.stream().filter(t -> t.getKey().equals(key)).forEach(t -> t.cancel());
    }

    /**
     * Determines if any task with {@code key} as their key attachment is
     * currently running.
     * 
     * @param key
     *            the key to determine this for.
     * @return {@code true} if there is a running task with that key attachment,
     *         {@code false} otherwise.
     */
    public static boolean running(Object key) {
        return PENDING_LIST.stream().anyMatch(t -> t.getKey().equals(key) && t.isRunning());
    }
}
