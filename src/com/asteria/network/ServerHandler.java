package com.asteria.network;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.asteria.Server;
import com.asteria.utility.LoggerUtils;
import com.google.common.base.Preconditions;

/**
 * The reactor that will oversee the management of all network events. In
 * contrast to other reactor implementations, this one runs on the main game
 * thread selecting and handling network events either asynchronously or right
 * on the underlying thread every {@code 600}ms.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ServerHandler {

    /**
     * The logger that will print important information.
     */
    private static Logger logger = LoggerUtils.getLogger(Server.class);

    /**
     * The selector that will determine which network events to handle.
     */
    private static Selector selector;

    /**
     * The server socket channel that the selector will be bound to.
     */
    private static ServerSocketChannel server;

    /**
     * The default constructor.
     *
     * @throws UnsupportedOperationException
     *             if this class is instantiated.
     */
    private ServerHandler() {
        throw new UnsupportedOperationException();
    }

    /**
     * Starts the server handler by opening and configuring the selector and
     * server socket channel.
     *
     * @param port
     *            the port to open this server handler on.
     * @throws Exception
     *             if any errors occur while starting the server handler.
     */
    public static void start(int port) throws Exception {
        Preconditions.checkState(server == null && selector == null);
        selector = Selector.open();
        server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.socket().bind(new InetSocketAddress(port));
        server.register(selector, SelectionKey.OP_ACCEPT);
    }

    /**
     * The sequencer for the reactor that will perform a non-blocking select on
     * available events and execute them.
     */
    public static void sequence() {
        try {
            selector.selectNow();
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                ServerSelectionKey key = new ServerSelectionKey(it.next());
                Optional<ServerSelectionEvent> event = key.determineEvent();
                event.ifPresent(e -> e.execute(key));
                it.remove();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error has occured while selecting " + "network events!", e);
        }
    }

    /**
     * Gets the selector that will determine which network events to handle.
     *
     * @return the selector to determine network events.
     */
    public static Selector getSelector() {
        return selector;
    }

    /**
     * Gets the server socket channel that the selector will be bound to.
     *
     * @return the server socket channel.
     */
    public static ServerSocketChannel getServer() {
        return server;
    }
}
