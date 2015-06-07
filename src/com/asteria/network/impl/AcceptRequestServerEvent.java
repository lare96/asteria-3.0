package com.asteria.network.impl;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

import com.asteria.game.character.player.PlayerIO;
import com.asteria.network.ConnectionHandler;
import com.asteria.network.ServerSelectionEvent;
import com.asteria.network.ServerSelectionKey;

/**
 * The server selection event that handles all incoming connections. This
 * asynchronous event accepts the connection, and registers it with the server's
 * selector.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class AcceptRequestServerEvent extends ServerSelectionEvent {

    /**
     * Creates a new {@link AcceptRequestServerEvent}.
     */
    public AcceptRequestServerEvent() {
        super(true);
    }

    /**
     * The counter that determines how many connections have been handled.
     */
    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public void executeEvent(ServerSelectionKey key) throws Exception {
        SocketChannel socket;
        while ((socket = key.getChannel().accept()) != null) {
            if (counter.getAndIncrement() > 5)
                break;
            String host = socket.socket().getInetAddress().getHostAddress();
            socket.configureBlocking(false);
            SelectionKey newKey = socket.register(key.getSelector(), SelectionKey.OP_READ);
            newKey.attach(new PlayerIO(newKey, ConnectionHandler.evaluate(host)));
        }
    }
}
