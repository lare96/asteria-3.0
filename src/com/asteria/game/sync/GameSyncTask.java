package com.asteria.game.sync;

import com.asteria.game.NodeType;
import com.asteria.game.World;
import com.google.common.base.Preconditions;

/**
 * A synchronization task executed under a {@link GameSyncExecutor}. The
 * character instance associated to {@code index} must be the mutex of a
 * synchronization block wrapped around the code.
 * 
 * @author lare96 <http://github.org/lare96>
 */
public abstract class GameSyncTask {

    /**
     * The amount of parties the synchronizer will register.
     */
    private final int amount;

    /**
     * The capacity of the backing character list.
     */
    private final int capacity;

    /**
     * The type of character using this synchronization task.
     */
    private final NodeType type;

    /**
     * The flag that determines if this should be executed concurrently.
     */
    private final boolean concurrent;

    /**
     * Creates a new {@link GameSyncTask}.
     *
     * @param type
     *            the type of character using this synchronization task.
     * @param concurrent
     *            the flag that determines if this should be executed
     *            concurrently.
     */
    public GameSyncTask(NodeType type, boolean concurrent) {
        Preconditions.checkArgument(type == NodeType.PLAYER || type == NodeType.NPC, "Invalid node type.");
        this.amount = type == NodeType.PLAYER ? World.getPlayers().size() : World.getNpcs().size();
        this.capacity = type == NodeType.PLAYER ? World.getPlayers().capacity() : World.getNpcs().capacity();
        this.type = type;
        this.concurrent = concurrent;
    }

    /**
     * Creates a new {@link GameSyncTask} that will be executed concurrently.
     *
     * @param type
     *            the type of character using this synchronization task.
     */
    public GameSyncTask(NodeType type) {
        this(type, true);
    }

    /**
     * Determines if the {@code index} about to be passed onto the
     * synchronization task logic is a valid character.
     * 
     * @param index
     *            the index about to be used.
     * @return {@code true} if the index is valid, {@code false} otherwise.
     */
    protected boolean checkIndex(int index) {
        return type == NodeType.PLAYER ? World.getPlayers().get(index) != null : World.getNpcs().get(index) != null;
    }

    /**
     * Executes the synchronization logic.
     * 
     * @param index
     *            the character index the synchronization is being done for.
     */
    public abstract void execute(final int index);

    /**
     * Gets the amount of parties the synchronizer will register.
     * 
     * @return the amount of parties.
     */
    public final int getAmount() {
        return amount;
    }

    /**
     * Gets the capacity of the backing character list.
     * 
     * @return the capacity of the backing list.
     */
    public final int getCapacity() {
        return capacity;
    }

    /**
     * Determines if this should be executed concurrently.
     * 
     * @return {@code true} if the task is concurrent, {@code false} otherwise.
     */
    public final boolean isConcurrent() {
        return concurrent;
    }
}
