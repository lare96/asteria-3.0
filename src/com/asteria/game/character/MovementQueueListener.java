package com.asteria.game.character;

import java.util.Objects;
import java.util.Optional;

import com.asteria.task.EventListener;
import com.asteria.task.TaskHandler;

/**
 * The container class that holds the movement queue listener. The listener
 * allows for various actions to be appended to the end of the movement queue,
 * this is useful for things such as "walking to actions".
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class MovementQueueListener {

    /**
     * The character this listener is dedicated to.
     */
    private final CharacterNode character;

    /**
     * The listener being used to execute tasks.
     */
    private Optional<MovementQueueListenerTask> listener = Optional.empty();

    /**
     * Creates a new {@link MovementQueueListener}.
     *
     * @param character
     *            the character this listener is dedicated to.
     */
    public MovementQueueListener(CharacterNode character) {
        this.character = character;
    }

    /**
     * Resets this {@link EventListener} so it may listen for the walking queue
     * to finish. Once the walking queue is finished the listener will run the
     * logic within {@code task}.
     * <p>
     * <p>
     * Please note that appended tasks are not guaranteed to be ran! If a new
     * task is being appended while the listener is already waiting to run
     * another task, the existing listener is stopped, the old task discarded,
     * and a new listener is started to run the new task.
     *
     * @param task
     *            the task that will be ran once the walking queue is finished.
     */
    public void append(Runnable task) {
        listener.ifPresent(t -> t.cancel());
        listener = Optional.of(new MovementQueueListenerTask(character, task));
        character.setFollowing(false);
        TaskHandler.submit(listener.get());
    }

    /**
     * The action listener implementation that allows for a task to be appended
     * to the end of the movement queue.
     *
     * @author lare96 <http://github.com/lare96>
     */
    private static final class MovementQueueListenerTask extends EventListener {

        /**
         * The character that the queued task will be ran for.
         */
        private final CharacterNode character;

        /**
         * The queued task that will be executed by this listener.
         */
        private final Runnable task;

        /**
         * Creates a new {@link MovementQueueListenerTask}.
         *
         * @param character
         *            the character that the queued task will be ran for.
         * @param task
         *            the queued task that will be executed by this listener.
         */
        public MovementQueueListenerTask(CharacterNode character, Runnable task) {
            this.character = character;
            this.task = Objects.requireNonNull(task);
        }

        @Override
        public boolean canExecute() {
            return character.getMovementQueue().isMovementDone();
        }

        @Override
        public void run() {
            if (character.isRegistered()) {
                try {
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}