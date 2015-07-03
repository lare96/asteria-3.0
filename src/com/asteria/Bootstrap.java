package com.asteria;

import com.asteria.game.GameBuilder;
import com.asteria.net.NetworkBuilder;

/**
 * The bootstrap that will prepare the game, network, and various utilities.
 * This class effectively enables Asteria to be put online.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class Bootstrap {

    /**
     * The port that the {@link NetworkBuilder} will listen for connections on.
     */
    private final int port;

    /**
     * The network builder that will initialize the core components of the
     * network.
     */
    private final NetworkBuilder networkBuilder = new NetworkBuilder();

    /**
     * The game builder that will initialize the core components of the game.
     */
    private final GameBuilder gameBuilder = new GameBuilder();

    /**
     * Creates a new {@link Bootstrap}.
     *
     * @param port
     *            the port that the network handler will listen on.
     */
    protected Bootstrap(int port) {
        this.port = port;
    }

    /**
     * Binds the core of the server together and puts Asteria online.
     *
     * @throws Exception
     *             if any errors occur while putting the server online.
     */
    public void bind() throws Exception {
        gameBuilder.initialize();
        networkBuilder.initialize(port);
    }
}