package com.asteria;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.asteria.game.GameService;
import com.asteria.game.character.player.content.RestoreStatTask;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.character.player.serialize.PlayerSerialization;
import com.asteria.game.item.ItemNodeManager;
import com.asteria.game.plugin.PluginHandler;
import com.asteria.network.ConnectionHandler;
import com.asteria.network.ServerHandler;
import com.asteria.task.TaskHandler;
import com.asteria.utility.json.ItemDefinitionLoader;
import com.asteria.utility.json.ItemNodeLoader;
import com.asteria.utility.json.NpcDefinitionLoader;
import com.asteria.utility.json.NpcDropTableLoader;
import com.asteria.utility.json.NpcNodeLoader;
import com.asteria.utility.json.ObjectNodeLoader;
import com.asteria.utility.json.ObjectNodeRemoveLoader;
import com.asteria.utility.json.PacketOpcodeLoader;
import com.asteria.utility.json.PacketSizeLoader;
import com.asteria.utility.json.ShopLoader;
import com.asteria.utility.json.WeaponAnimationLoader;
import com.asteria.utility.json.WeaponInterfaceLoader;
import com.asteria.utility.json.WeaponPoisonLoader;
import com.asteria.utility.json.WeaponRequirementLoader;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * The bootstrap that will prepare the game, network, and various utilities.
 * This class effectively enables Asteria to be put online.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ServerBootstrap {

    /**
     * The executor service that will load various utilities in the background
     * while the rest of the server is being constructed.
     */
    private final ExecutorService serviceLoader = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat(
        "ServiceLoaderThread").build());

    /**
     * The scheduled executor service that will run the {@link GameService}.
     */
    private final ScheduledExecutorService sequencer = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat(
        "GameThread").build());

    /**
     * The port that this server builder will bind the network on.
     */
    private final int port;

    /**
     * The default constructor with a {@code protected} access modifier, to
     * restrict the instantiation of this class to the {@code com.asteria}
     * package.
     * 
     * @param port
     *            the port that this server builder will bind the network on.
     */
    protected ServerBootstrap(int port) {
        this.port = port;
    }

    /**
     * Binds the entire server together (hence the name "server bootstrap")
     * which consists of loading utilities, starting the network, and executing
     * the game sequence. The utilities are loaded in the background while other
     * functions are prepared.
     *
     * @throws Exception
     *             if any errors occur while binding, or if the background
     *             service load takes too long.
     */
    public void bind() throws Exception {
        Preconditions.checkState(!serviceLoader.isShutdown(), "The bootstrap has been bound already!");
        executeServiceLoad();
        ServerHandler.start(port);
        serviceLoader.shutdown();
        if (!serviceLoader.awaitTermination(15, TimeUnit.MINUTES))
            throw new IllegalStateException("The background service load took too long!");
        sequencer.scheduleAtFixedRate(new GameService(), 0, 600, TimeUnit.MILLISECONDS);
        TaskHandler.submit(new ItemNodeManager());
        TaskHandler.submit(new RestoreStatTask());
        TaskHandler.submit(new MinigameHandler());
        PlayerSerialization.getCache().init();
    }

    /**
     * Submits all of the utilities to the service loader to be loaded in the
     * background. Please note that the loader may use multiple threads to load
     * the utilities concurrently, so utilities that depend on each other
     * <b>must</b> be executed in the same task.
     */
    private void executeServiceLoad() {
        serviceLoader.execute(() -> new NpcDefinitionLoader().load());
        serviceLoader.execute(() -> new ItemDefinitionLoader().load());
        serviceLoader.execute(() -> new WeaponPoisonLoader().load());
        serviceLoader.execute(() -> new PacketOpcodeLoader().load());
        serviceLoader.execute(() -> new PacketSizeLoader().load());
        serviceLoader.execute(ConnectionHandler::parseIPBans);
        serviceLoader.execute(() -> new NpcNodeLoader().load());
        serviceLoader.execute(() -> new ShopLoader().load());
        serviceLoader.execute(() -> new ItemNodeLoader().load());
        serviceLoader.execute(() -> new ObjectNodeLoader().load());
        serviceLoader.execute(() -> new NpcDropTableLoader().load());
        serviceLoader.execute(() -> new WeaponAnimationLoader().load());
        serviceLoader.execute(() -> new WeaponInterfaceLoader().load());
        serviceLoader.execute(() -> new WeaponRequirementLoader().load());
        serviceLoader.execute(() -> new ObjectNodeRemoveLoader().load());
        serviceLoader.execute(PluginHandler::init);
    }
}