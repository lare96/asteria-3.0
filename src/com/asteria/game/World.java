package com.asteria.game;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import plugin.minigames.fightcaves.FightCavesHandler;

import com.asteria.game.character.CharacterList;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.npc.NpcUpdating;
import com.asteria.game.character.player.IOState;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.PlayerUpdating;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.item.ItemNodeManager;
import com.asteria.game.location.Position;
import com.asteria.game.object.ObjectNodeManager;
import com.asteria.game.plugin.PluginHandler;
import com.asteria.game.shop.Shop;
import com.asteria.game.sync.GameSyncExecutor;
import com.asteria.game.sync.GameSyncTask;
import com.asteria.net.ConnectionHandler;
import com.asteria.net.PlayerIO;
import com.asteria.task.Task;
import com.asteria.task.TaskQueue;
import com.asteria.utility.LoggerUtils;

/**
 * The static utility class that contains functions to manage and process game
 * characters.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class World {

    /**
     * The logger that will print important information.
     */
    private static Logger logger = LoggerUtils.getLogger(World.class);

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
     * The queue of {@link Player}s waiting to be logged in.
     */
    private static Queue<Player> logins = new ConcurrentLinkedQueue<>();

    /**
     * The queue of {@link Player}s waiting to be logged out.
     */
    private static Queue<Player> logouts = new ConcurrentLinkedQueue<>();

    /**
     * The manager for the map of game plugins.
     */
    private static PluginHandler plugins = new PluginHandler();

    /**
     * The manger for game synchronization.
     */
    private static GameSyncExecutor executor = new GameSyncExecutor();

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

        // Handle queued logins.
        for (int amount = 0; amount < GameConstants.LOGIN_THRESHOLD; amount++) {
            Player player = logins.poll();
            if (player == null)
                break;
            if (!players.add(player))
                player.dispose();
        }

        // Handle queued logouts.
        int amount = 0;
        Iterator<Player> $it = logouts.iterator();
        while ($it.hasNext()) {
            Player player = $it.next();
            if (player == null || amount >= GameConstants.LOGIN_THRESHOLD)
                break;
            if (handleLogout(player)) {
                $it.remove();
                amount++;
            }
        }

        // Handle task processing.
        taskQueue.sequence();

        // Handle synchronization tasks.
        executor.sync(new GameSyncTask(NodeType.PLAYER, false) {
            @Override
            public void execute(int index) {
                Player player = players.get(index);
                try {
                    player.getSession().handleQueuedMessages();
                    player.getMovementQueue().sequence();
                    player.sequence();
                } catch (Exception e) {
                    e.printStackTrace();
                    World.getPlayers().remove(player);
                }
            }
        });

        executor.sync(new GameSyncTask(NodeType.NPC, false) {
            @Override
            public void execute(int index) {
                Npc npc = npcs.get(index);
                try {
                    npc.sequence();
                    npc.getMovementQueue().sequence();
                } catch (Exception e) {
                    e.printStackTrace();
                    World.getNpcs().remove(npc);
                }
            }
        });

        executor.sync(new GameSyncTask(NodeType.PLAYER) {
            @Override
            public void execute(int index) {
                Player player = players.get(index);
                synchronized (player) {
                    try {
                        PlayerUpdating.update(player);
                        NpcUpdating.update(player);
                    } catch (Exception e) {
                        e.printStackTrace();
                        World.getPlayers().remove(player);
                    }
                }
            }
        });

        executor.sync(new GameSyncTask(NodeType.PLAYER) {
            @Override
            public void execute(int index) {
                Player player = players.get(index);
                synchronized (player) {
                    try {
                        player.reset();
                        player.setCachedUpdateBlock(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                        World.getPlayers().remove(player);
                    }
                }
            }
        });

        executor.sync(new GameSyncTask(NodeType.NPC) {
            @Override
            public void execute(int index) {
                Npc npc = npcs.get(index);
                synchronized (npc) {
                    try {
                        npc.reset();
                    } catch (Exception e) {
                        e.printStackTrace();
                        World.getNpcs().remove(npc);
                    }
                }
            }
        });
    }

    /**
     * Queues {@code player} to be logged in on the next server sequence.
     * 
     * @param player
     *            the player to log in.
     */
    public static void queueLogin(Player player) {
        PlayerIO session = player.getSession();
        if (session.getState() == IOState.LOGGING_IN && !logins.contains(player))
            logins.add(player);
    }

    /**
     * Queues {@code player} to be logged out on the next server sequence.
     * 
     * @param player
     *            the player to log out.
     */
    public static void queueLogout(Player player) {
        PlayerIO session = player.getSession();
        if (session.getState() == IOState.LOGGED_IN && !logouts.contains(player)) {
            if (player.getCombatBuilder().inCombat())
                player.getLogoutTimer().reset();
            logouts.add(player);
        }
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
     * Retrieves and returns the local {@link Player}s for {@code character}.
     * The specific players returned is completely dependent on the character
     * given in the argument.
     * 
     * @param character
     *            the character that it will be returned for.
     * @return the local players.
     */
    public static Iterator<Player> getLocalPlayers(CharacterNode character) {
        if (character.getType() == NodeType.PLAYER)
            return ((Player) character).getLocalPlayers().iterator();
        return players.iterator();
    }

    /**
     * Retrieves and returns the local {@link Npc}s for {@code character}. The
     * specific npcs returned is completely dependent on the character given in
     * the argument.
     * 
     * @param character
     *            the character that it will be returned for.
     * @return the local npcs.
     */
    public static Iterator<Npc> getLocalNpcs(CharacterNode character) {
        if (character.getType() == NodeType.PLAYER)
            return ((Player) character).getLocalNpcs().iterator();
        return npcs.iterator();
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
        players.forEach(p -> p.getMessages().sendMessage("@red@[ANNOUNCEMENT]: " + message));
    }

    /**
     * Performs all of the disconnection logic for {@code player} assuming they
     * are in the logout queue.
     * 
     * @param player
     *            the player to attempt to logout.
     * @return {@code true} if the player was logged out, {@code false}
     *         otherwise.
     */
    private static boolean handleLogout(Player player) {
        try {
            PlayerIO session = player.getSession();

            // Close the channel no matter what happens, so it appears to the
            // player that they have logged out.
            session.getChannel().close();

            // If the player x-logged, don't log the player out. Keep the
            // player queued until they are out of combat to prevent x-logging.
            if (!player.getLogoutTimer().elapsed(GameConstants.INVALID_LOGOUT_SECONDS, TimeUnit.SECONDS) || player.getCombatBuilder()
                .inCombat())
                return false;

            // Proceed to perform disconnection logic as normal, officially
            // logging out the player.
            session.setState(IOState.LOGGING_OUT);
            if (player.getOpenShop() != null)
                Shop.SHOPS.get(player.getOpenShop()).getPlayers().remove(player);
            World.getTaskQueue().cancel(player.getCombatBuilder());
            World.getTaskQueue().cancel(player);
            player.setSkillAction(false);
            World.getPlayers().remove(player);
            MinigameHandler.execute(player, m -> m.onLogout(player));
            player.getTradeSession().reset(false);
            player.getPrivateMessage().updateOtherList(false);
            if (FightCavesHandler.remove(player))
                player.move(new Position(2399, 5177));
            player.save();
            ConnectionHandler.remove(session.getHost());
            session.setState(IOState.LOGGED_OUT);
            logger.info(session + " has logged out.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
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
     * Gets the manager for the map of game plugins.
     * 
     * @return the manager for plugins.
     */
    public static PluginHandler getPlugins() {
        return plugins;
    }
}