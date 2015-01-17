package com.asteria.network.impl;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

import com.asteria.game.character.player.PlayerIO;
import com.asteria.network.ConnectionHandler;
import com.asteria.network.ServerHandler;
import com.asteria.network.ServerSelectionEvent;
import com.asteria.network.ServerSelectionKey;

/**
 * The server selection event that handles all incoming connections.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
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
        while ((socket = ServerHandler.getServer().accept()) != null || counter.getAndIncrement() <= 5) {
            if (socket == null) {
                continue;
            }
            String host = socket.socket().getInetAddress().getHostAddress();
            socket.configureBlocking(false);
            SelectionKey newKey = socket.register(ServerHandler.getSelector(), SelectionKey.OP_READ);
            newKey.attach(new PlayerIO(newKey, ConnectionHandler.evaluate(host)));
        }
    }
}
