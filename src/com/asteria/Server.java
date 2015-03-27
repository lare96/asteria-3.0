package com.asteria;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.asteria.game.character.player.content.RestoreStatTask;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.character.player.serialize.PlayerSerialization;
import com.asteria.game.item.ItemNodeManager;
import com.asteria.task.TaskHandler;
import com.asteria.utility.LoggerUtils;
import com.asteria.utility.Settings;

/**
 * The main class that will prepare the server builder and put the server
 * online.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public final class Server {

    /**
     * The logger that will print important information.
     */
    private static Logger logger = LoggerUtils.getLogger(Server.class);

    /**
     * The server builder that will prepare the server.
     */
    private static ServerBuilder builder;

    /**
     * The default constructor.
     * 
     * @throws UnsupportedOperationException
     *             if this class is instantiated.
     */
    private Server() {
        throw new UnsupportedOperationException("This class cannot be instantiated!");
    }

    /**
     * The main method that will put the server online.
     * 
     * @param args
     *            the runtime arguments.
     */
    public static void main(String[] args) {
        try {
            boolean concurrent = (Runtime.getRuntime().availableProcessors() > 1);
            builder = new ServerBuilder().setParallelEngine(concurrent).setServerPort(Settings.PORT);
            builder.build();

            logger.info(Settings.NAME + " is now online!");
            TaskHandler.submit(new ItemNodeManager());
            TaskHandler.submit(new RestoreStatTask());
            TaskHandler.submit(new MinigameHandler());
            PlayerSerialization.getCache().init();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred while starting " + Settings.NAME + "!", e);
            System.exit(1);
        }
    }

    /**
     * Gets the server builder that will prepare the server.
     * 
     * @return the server builder.
     */
    public static ServerBuilder getBuilder() {
        return builder;
    }
}