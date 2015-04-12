package com.asteria;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.asteria.utility.LoggerUtils;
import com.asteria.utility.Settings;
import com.google.common.base.Preconditions;

/**
 * The main class that will create and bind the {@link ServerBootstrap},
 * effectively putting the server online.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class Server {

    /**
     * The logger that will print important information.
     */
    private static Logger logger = LoggerUtils.getLogger(Server.class);

    /**
     * The default constructor, will throw an
     * {@link UnsupportedOperationException} if instantiated.
     *
     * @throws UnsupportedOperationException
     *             if this class is instantiated.
     */
    private Server() {
        throw new UnsupportedOperationException("This class cannot be instantiated!");
    }

    /**
     * The main method that will put the server online.
     */
    public static void main(String[] args) {
        try {
            Preconditions.checkState(args.length == 0, "No runtime arguments needed!");
            ServerBootstrap bootstrap = new ServerBootstrap(Settings.PORT);
            bootstrap.bind();
            logger.info(Settings.NAME + " is now online!");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred while binding the bootstrap!", e);

            // No point in continuing server startup when the
            // bootstrap either failed to bind or was bound
            // incorrectly.
            System.exit(1);
        }
    }
}