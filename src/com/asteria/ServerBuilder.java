package com.asteria;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.asteria.game.GameService;
import com.asteria.game.character.player.Player;
import com.asteria.network.ConnectionHandler;
import com.asteria.network.ServerHandler;
import com.asteria.utility.Settings;
import com.asteria.utility.json.ItemDefinitionLoader;
import com.asteria.utility.json.ItemNodeLoader;
import com.asteria.utility.json.MinigameLoader;
import com.asteria.utility.json.NpcDefinitionLoader;
import com.asteria.utility.json.NpcDropTableLoader;
import com.asteria.utility.json.NpcNodeLoader;
import com.asteria.utility.json.ObjectNodeLoader;
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
 * The builder class that will prepare the game, network, and load various
 * utilities.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class ServerBuilder {

    /**
     * The executor service that will load various utilities in the background
     * while the rest of the server is being constructed.
     */
    private final ExecutorService serviceLoader = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat(
        "ServiceLoaderThread").build());

    /**
     * The scheduled executor service that will run the {@link GameService}.
     */
    private final ScheduledExecutorService sequencer = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder()
        .setNameFormat("GameThread").build());

    /**
     * The flag that determines if the engine should update {@link Player}s in
     * parallel.
     */
    private boolean parallelEngine = true;

    /**
     * The port that this server builder will bind the network on.
     */
    private int serverPort = Settings.PORT;

    /**
     * The default constructor with a {@code protected} access modifier, to
     * restrict the instantiation of this class to the {@code com.asteria}
     * package.
     */
    protected ServerBuilder() {

    }

    @Override
    public String toString() {
        return "SERVER_BUILDER[port= " + serverPort + ", concurrent= " + parallelEngine + "]";
    }

    /**
     * Builds the entire server which consists of loading the utilities, binding
     * the network, and executing the game sequence. The utilities are loaded in
     * the background while other functions are prepared.
     * 
     * @throws Exception
     *             if any errors occur while building the server, or if the
     *             background service load takes too long.
     */
    public void build() throws Exception {
        Preconditions.checkState(!serviceLoader.isShutdown(), "The server has been started already!");
        executeServiceLoad();

        ServerHandler.start(serverPort);
        sequencer.scheduleAtFixedRate(new GameService(), 0, 600, TimeUnit.MILLISECONDS);

        serviceLoader.shutdown();
        if (!serviceLoader.awaitTermination(15, TimeUnit.MINUTES)) {
            throw new IllegalStateException("The background service load took too long!");
        }
    }

    /**
     * Submits all of the utilities to the {@link ServerBuilder#serviceLoader}
     * to be loaded in the background. Please note that the loader uses multiple
     * threads to load the utilities concurrently, so code must be thread safe.
     */
    private void executeServiceLoad() {
        serviceLoader.execute(() -> new NpcDefinitionLoader().load());
        serviceLoader.execute(() -> new ItemDefinitionLoader().load());
        serviceLoader.execute(() -> new WeaponPoisonLoader().load());
        serviceLoader.execute(() -> new PacketOpcodeLoader().load());
        serviceLoader.execute(() -> new PacketSizeLoader().load());
        serviceLoader.execute(() -> new MinigameLoader().load());
        serviceLoader.execute(() -> ConnectionHandler.parseIPBans());
        serviceLoader.execute(() -> new NpcNodeLoader().load());
        serviceLoader.execute(() -> new ShopLoader().load());
        serviceLoader.execute(() -> new ItemNodeLoader().load());
        serviceLoader.execute(() -> new ObjectNodeLoader().load());
        serviceLoader.execute(() -> new NpcDropTableLoader().load());
        serviceLoader.execute(() -> new WeaponAnimationLoader().load());
        serviceLoader.execute(() -> new WeaponInterfaceLoader().load());
        serviceLoader.execute(() -> new WeaponRequirementLoader().load());
    }

    /**
     * Determines if the engine should update {@link Player}s in parallel.
     * 
     * @return {@code true} if the engine should update players in parallel,
     *         {@code false} otherwise.
     */
    public boolean isParallelEngine() {
        return parallelEngine;
    }

    /**
     * Sets the value for {@link ServerBuilder#parallelEngine}.
     * 
     * @param parallelEngine
     *            the new value to set.
     * @return an instance of this builder, for chaining.
     */
    protected ServerBuilder setParallelEngine(boolean parallelEngine) {
        this.parallelEngine = parallelEngine;
        return this;
    }

    /**
     * Gets the port that this server builder will bind the network on.
     * 
     * @return the port for binding this network.
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * Sets the value for {@link ServerBuilder#serverPort}.
     * 
     * @param serverPort
     *            the new value to set.
     * @return an instance of this builder, for chaining.
     */
    protected ServerBuilder setServerPort(int serverPort) {
        this.serverPort = serverPort;
        return this;
    }
}