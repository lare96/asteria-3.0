package com.asteria.game;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.asteria.game.character.player.content.RestoreStatTask;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.character.player.serialize.PlayerSerialization;
import com.asteria.game.item.ItemNodeManager;
import com.asteria.game.task.TaskHandler;
import com.asteria.network.ServerReactor;
import com.asteria.utility.Settings;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * Initializes game {@link java.lang.Object}s and prepares them to be created by
 * the {@link com.asteria.ServerBootstrap}.
 * 
 * @author lare96 <http://github.org/lare96>
 */
public final class GameBuilder {

    /**
     * The executor that will run the {@link com.asteria.game.GameService} every
     * {@code CYCLE_RATE}ms.
     */
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat(
        "GameThread").build());

    /**
     * Creates the game by submitting the game service it to an executor that
     * will run it every {@code CYCLE_RATE}ms, as well as executing any network
     * and worldly logic that needs to be initialized.
     * 
     * @param port
     *            the port that the server will listen on.
     * @throws Exception
     *             if any errors occur whilst creating the game.
     */
    public void create(int port) throws Exception {
        Selector selector = Selector.open();
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.socket().bind(new InetSocketAddress(port));
        channel.register(selector, SelectionKey.OP_ACCEPT);
        World.getService().setServerReactor(new ServerReactor(selector, channel));
        executor.scheduleAtFixedRate(World.getService(), 0, Settings.CYCLE_RATE, TimeUnit.MILLISECONDS);
        TaskHandler.submit(new ItemNodeManager());
        TaskHandler.submit(new RestoreStatTask());
        TaskHandler.submit(new MinigameHandler());
        PlayerSerialization.getCache().init();
    }
}
