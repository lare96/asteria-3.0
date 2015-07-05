package com.asteria.game;

import java.util.ArrayDeque;
import java.util.Queue;

import com.asteria.Bootstrap;
import com.asteria.game.character.player.content.RestoreStatTask;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.character.player.serialize.PlayerSerialization;
import com.asteria.game.item.ItemNodeManager;
import com.asteria.net.ConnectionHandler;
import com.asteria.service.ServiceQueue;
import com.asteria.utility.BackgroundLoader;
import com.asteria.utility.json.EquipmentRequirementLoader;
import com.asteria.utility.json.ItemDefinitionLoader;
import com.asteria.utility.json.ItemNodeLoader;
import com.asteria.utility.json.MessageOpcodeLoader;
import com.asteria.utility.json.MessageSizeLoader;
import com.asteria.utility.json.NpcDefinitionLoader;
import com.asteria.utility.json.NpcDropCacheLoader;
import com.asteria.utility.json.NpcDropTableLoader;
import com.asteria.utility.json.NpcNodeLoader;
import com.asteria.utility.json.ObjectNodeLoader;
import com.asteria.utility.json.ObjectNodeRemoveLoader;
import com.asteria.utility.json.ShopLoader;
import com.asteria.utility.json.WeaponAnimationLoader;
import com.asteria.utility.json.WeaponInterfaceLoader;
import com.asteria.utility.json.WeaponPoisonLoader;

/**
 * Initializes game {@link Object}s and prepares them to be created by the
 * {@link Bootstrap}.
 * 
 * @author lare96 <http://github.org/lare96>
 */
public final class GameBuilder {

    /**
     * The background loader that will load various utilities in the background
     * while the bootstrap is preparing the server.
     */
    private final BackgroundLoader backgroundLoader = new BackgroundLoader();

    /**
     * The service queue that will run the {@link GameService}.
     */
    private final ServiceQueue queue = new ServiceQueue();

    /**
     * Initializes this game builder effectively preparing the background
     * startup tasks and game processing.
     *
     * @throws Exception
     *             if any issues occur while starting the network.
     */
    public void initialize() throws Exception {
        backgroundLoader.start(createBackgroundTasks());
        queue.submit(World.getService());
        World.submit(new ItemNodeManager());
        World.submit(new RestoreStatTask());
        World.submit(new MinigameHandler());
        PlayerSerialization.getCache().init();
        if (!backgroundLoader.awaitCompletion())
            throw new IllegalStateException("Background load did not complete normally!");
    }

    /**
     * Returns a queue containing all of the background tasks that will be
     * executed by the background loader. Please note that the loader may use
     * multiple threads to load the utilities concurrently, so utilities that
     * depend on each other <b>must</b> be executed in the same task to ensure
     * thread safety.
     *
     * @return the queue of background tasks.
     */
    public Queue<Runnable> createBackgroundTasks() {
        Queue<Runnable> tasks = new ArrayDeque<>();
        tasks.add(() -> new NpcDefinitionLoader().load());
        tasks.add(() -> new ItemDefinitionLoader().load());
        tasks.add(() -> new WeaponPoisonLoader().load());
        tasks.add(() -> new MessageOpcodeLoader().load());
        tasks.add(() -> new MessageSizeLoader().load());
        tasks.add(ConnectionHandler::parseIPBans);
        tasks.add(() -> new NpcNodeLoader().load());
        tasks.add(() -> new ShopLoader().load());
        tasks.add(() -> new ItemNodeLoader().load());
        tasks.add(() -> new ObjectNodeLoader().load());
        tasks.add(() -> new NpcDropTableLoader().load());
        tasks.add(() -> new WeaponAnimationLoader().load());
        tasks.add(() -> new WeaponInterfaceLoader().load());
        tasks.add(() -> new EquipmentRequirementLoader().load());
        tasks.add(() -> new ObjectNodeRemoveLoader().load());
        tasks.add(() -> new NpcDropCacheLoader().load());
        tasks.add(World.getPlugins()::init);
        return tasks;
    }
}
