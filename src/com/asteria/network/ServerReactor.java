package com.asteria.network;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.asteria.utility.LoggerUtils;

/**
 * The reactor that will oversee the management of all network events. In
 * contrast to other reactor implementations, this one runs on the main game
 * thread selecting and handling network events either asynchronously or right
 * on the underlying thread.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ServerReactor {

    /**
     * The logger that will print important information.
     */
    private final Logger logger = LoggerUtils.getLogger(ServerReactor.class);

    /**
     * The selector that will select network events.
     */
    private final Selector selector;

    /**
     * The socket channel that the server will listen on.
     */
    private final ServerSocketChannel channel;

    /**
     * Creates a new {@link ServerReactor}.
     *
     * @param selector
     *            the selector that will select network events.
     * @param channel
     *            the socket channel that the server will listen on.
     */
    public ServerReactor(Selector selector, ServerSocketChannel channel) {
        this.selector = selector;
        this.channel = channel;
    }

    /**
     * Loops through all of the selected network events and determines which
     * ones are ready to be handled, and executes them if so.
     */
    public void sequence() {
        try {
            selector.selectNow();
            Iterator<SelectionKey> $it = selector.selectedKeys().iterator();
            while ($it.hasNext()) {
                ServerSelectionKey key = new ServerSelectionKey($it.next(), selector, channel);
                Optional<ServerSelectionEvent> event = key.determineEvent();
                event.ifPresent(e -> e.execute(key));
                $it.remove();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error has occured while selecting network events!", e);
        }
    }
}
