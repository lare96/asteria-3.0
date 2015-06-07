package com.asteria.game;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

import com.asteria.game.character.CharacterList;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.npc.NpcUpdating;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.PlayerUpdating;
import com.asteria.game.item.ItemNodeManager;
import com.asteria.game.object.ObjectNodeManager;
import com.asteria.task.Task;
import com.asteria.task.TaskQueue;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * The static utility class that contains functions to manage and process game
 * characters.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class World {

    /**
     * The flag that determines if processing should be parallelized.
     */
    private static final boolean PARALLEL_PROCESSING = (Runtime.getRuntime().availableProcessors() > 1);

    /**
     * The collection of active players.
     */
    private static CharacterList<Player> players = new CharacterList<>(2000);

    /**
     * The collection of active NPCs.
     */
    private static CharacterList<Npc> npcs = new CharacterList<>(5000);

    /**
     * The game service that processes this world.
     */
    private static GameService service = new GameService();

    /**
     * The manager for the queue of game tasks.
     */
    private static TaskQueue taskQueue = new TaskQueue();

    /**
     * The default constructor, will throw an
     * {@link UnsupportedOperationException} if instantiated.
     *
     * @throws UnsupportedOperationException
     *             if this class is instantiated.
     */
    private World() {
        throw new UnsupportedOperationException("This class cannot be instantiated!");
    }

    /**
     * The method that executes the update sequence for all in game characters
     * every cycle. The update sequence may either run sequentially or
     * concurrently depending on the type of engine selected by the server.
     *
     * @throws Exception
     *             if any errors occur during the update sequence.
     */
    public static void sequence() throws Exception {
        Runnable updateService = PARALLEL_PROCESSING ? new ConcurrentUpdateService() : new SequentialUpdateService();
        updateService.run();
    }

    /**
     * Submits {@code t} to the backing {@link TaskQueue}.
     * 
     * @param t
     *            the task to submit to the queue.
     */
    public static void submit(Task t) {
        taskQueue.submit(t);
    }

    /**
     * Returns a player within an optional whose name hash is equal to
     * {@code username}.
     *
     * @param username
     *            the name hash to check the collection of players for.
     * @return the player within an optional if found, or an empty optional if
     *         not found.
     */
    public static Optional<Player> getPlayer(long username) {
        return players.search(player -> player.getUsernameHash() == username);
    }

    /**
     * Returns a player within an optional whose name is equal to
     * {@code username}.
     *
     * @param username
     *            the name to check the collection of players for.
     * @return the player within an optional if found, or an empty optional if
     *         not found.
     */
    public static Optional<Player> getPlayer(String username) {
        if (username == null)
            return Optional.empty();
        return players.search(player -> player.getUsername().equals(username));
    }

    /**
     * Gets every single character in the player and npc character lists.
     *
     * @return a set containing every single character.
     */
    public static Set<CharacterNode> getCharacters() {
        Set<CharacterNode> characters = new HashSet<>();
        players.forEach(characters::add);
        npcs.forEach(characters::add);
        return characters;
    }

    /**
     * Gets every single node in the player, npc, object, and item lists.
     *
     * @return a list containing every single node.
     */
    public static List<Node> getNodes() {
        List<Node> nodes = new LinkedList<>();
        players.forEach(nodes::add);
        npcs.forEach(nodes::add);
        ObjectNodeManager.OBJECTS.forEach(nodes::add);
        ItemNodeManager.ITEMS.forEach(nodes::add);
        return nodes;
    }

    /**
     * Sends {@code message} to all online players.
     *
     * @param message
     *            the message to send to all online players.
     */
    public static void message(String message) {
        players.forEach(p -> p.getEncoder().sendMessage("@red@[ANNOUNCEMENT]: " + message));
    }

    /**
     * Gets the collection of active players.
     *
     * @return the active players.
     */
    public static CharacterList<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the collection of active npcs.
     *
     * @return the active npcs.
     */
    public static CharacterList<Npc> getNpcs() {
        return npcs;
    }

    /**
     * Returns the game service that processes this world.
     * 
     * @return the game service.
     */
    public static GameService getService() {
        return service;
    }

    /**
     * Gets the manager for the queue of game tasks.
     * 
     * @return the queue of tasks.
     */
    public static TaskQueue getTaskQueue() {
        return taskQueue;
    }

    /**
     * Sets the value for {@link World.java#taskQueue}.
     * 
     * @param taskQueue
     *            the new value to set.
     */
    public static void setTaskQueue(TaskQueue taskQueue) {
        World.taskQueue = taskQueue;
    }

    /**
     * The concurrent update service that will execute the update sequence in
     * parallel using {@link Runtime#availableProcessors()} threads. If the
     * hosting computer has more than one core, is it guaranteed that this
     * update service will perform better than {@link SequentialUpdateService}.
     *
     * @author lare96 <http://github.com/lare96>
     */
    private static final class ConcurrentUpdateService implements Runnable {

        /**
         * The phaser keeps the entire update sequence in proper synchronization
         * with the main game thread.
         */
        private final Phaser synchronizer = new Phaser(1);

        /**
         * The executor that allows us to utilize multiple threads to update in
         * parallel.
         */
        private final ExecutorService updateService = ConcurrentUpdateService.create();

        @Override
        public void run() {

            // Sequence movement and perform sequential processing for players.
            World.getPlayers().forEach(player -> {
                try {
                    player.sequence();
                    player.getMovementQueue().sequence();
                } catch (Exception e) {
                    e.printStackTrace();
                    World.getPlayers().remove(player);
                }
            });

            // Sequence movement and perform sequential processing for npcs.
            World.getNpcs().forEach(npc -> {
                try {
                    npc.sequence();
                    npc.getMovementQueue().sequence();
                } catch (Exception e) {
                    e.printStackTrace();
                    World.getNpcs().remove(npc);
                }
            });

            // Update players for players, and npcs for players.
            synchronizer.bulkRegister(World.getPlayers().size());
            World.getPlayers().forEach(player -> updateService.execute(() -> {
                synchronized (player) {
                    try {
                        PlayerUpdating.update(player);
                        NpcUpdating.update(player);
                    } catch (Exception e) {
                        e.printStackTrace();
                        World.getPlayers().remove(player);
                    } finally {
                        synchronizer.arriveAndDeregister();
                    }
                }
            }));
            synchronizer.arriveAndAwaitAdvance();

            // Prepare players for the next update sequence.
            synchronizer.bulkRegister(World.getPlayers().size());
            World.getPlayers().forEach(player -> updateService.execute(() -> {
                synchronized (player) {
                    try {
                        player.reset();
                        player.setCachedUpdateBlock(null);
                        player.getSession().getPacketCount().set(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        World.getPlayers().remove(player);
                    } finally {
                        synchronizer.arriveAndDeregister();
                    }
                }
            }));
            synchronizer.arriveAndAwaitAdvance();

            // Prepare npcs for the next update sequence.
            synchronizer.bulkRegister(World.getNpcs().size());
            World.getNpcs().forEach(npc -> updateService.execute(() -> {
                synchronized (npc) {
                    try {
                        npc.reset();
                    } catch (Exception e) {
                        e.printStackTrace();
                        World.getNpcs().remove(npc);
                    } finally {
                        synchronizer.arriveAndDeregister();
                    }
                }
            }));
            synchronizer.arriveAndAwaitAdvance();
        }

        /**
         * Creates and configures the update service that will execute updating
         * in parallel for characters. The returned executor is
         * <b>unconfigurable</b> meaning it's configuration can no longer be
         * modified.
         *
         * @return the newly created and configured update service.
         */
        private static ExecutorService create() {
            int nThreads = Runtime.getRuntime().availableProcessors();
            ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(nThreads);
            executor.setRejectedExecutionHandler(new CallerRunsPolicy());
            executor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("UpdateServiceThread").build());
            return Executors.unconfigurableScheduledExecutorService(executor);
        }
    }

    /**
     * The sequential update service that will execute the update sequence
     * sequentially. This service should only be used if the hosting computer
     * has one core. If the hosting computer has more than one core, better
     * performance is guaranteed with {@link ConcurrentUpdateService}.
     *
     * @author lare96 <http://github.com/lare96>
     */
    private static final class SequentialUpdateService implements Runnable {

        @Override
        public void run() {

            // Update movement for players.
            World.getPlayers().forEach(player -> {
                try {
                    player.sequence();
                    player.getMovementQueue().sequence();
                } catch (Exception e) {
                    e.printStackTrace();
                    World.getPlayers().remove(player);
                }
            });

            // Update movement for npcs.
            World.getNpcs().forEach(npc -> {
                try {
                    npc.sequence();
                    npc.getMovementQueue().sequence();
                } catch (Exception e) {
                    e.printStackTrace();
                    World.getNpcs().remove(npc);
                }
            });

            // Update players for players, and npcs for players.
            World.getPlayers().forEach(player -> {
                try {
                    PlayerUpdating.update(player);
                    NpcUpdating.update(player);
                } catch (Exception e) {
                    e.printStackTrace();
                    World.getPlayers().remove(player);
                }
            });

            // Prepare players for the next update sequence.
            World.getPlayers().forEach(player -> {
                try {
                    player.reset();
                    player.setCachedUpdateBlock(null);
                    player.getSession().getPacketCount().set(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    World.getPlayers().remove(player);
                }
            });

            // Prepare npcs for the next update sequence.
            World.getNpcs().forEach(npc -> {
                try {
                    npc.reset();
                } catch (Exception e) {
                    e.printStackTrace();
                    World.getNpcs().remove(npc);
                }
            });
        }
    }
}