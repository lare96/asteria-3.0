package com.asteria.network;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Optional;

import com.asteria.network.impl.AcceptRequestServerEvent;
import com.asteria.network.impl.DecodePacketServerEvent;

/**
 * The container class that serves as a wrapper for selection keys. Its main
 * purpose is to determine which type of networking event should be executed for
 * this key.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ServerSelectionKey {

    /**
     * The internal selection key wrapped by this container.
     */
    private final SelectionKey key;

    /**
     * The internal selector that determined this network event, wrapped by this
     * container.
     */
    private final Selector selector;

    /**
     * The internal channel that the server is listening on, wrapped by this
     * container.
     */
    private final ServerSocketChannel channel;

    /**
     * Creates a new {@link ServerSelectionKey}.
     *
     * @param key
     *            the internal selection key wrapped by this container.
     */
    public ServerSelectionKey(SelectionKey key, Selector selector, ServerSocketChannel channel) {
        this.key = key;
        this.selector = selector;
        this.channel = channel;
    }

    /**
     * Determines the event that this key is ready for.
     *
     * @return the event wrapped in an optional, or an empty optional if this
     *         key is not ready for any events.
     */
    public Optional<ServerSelectionEvent> determineEvent() {
        if (!key.isValid())
            return Optional.empty();
        if (key.isAcceptable()) {
            return Optional.of(new AcceptRequestServerEvent());
        } else if (key.isReadable()) {
            return Optional.of(new DecodePacketServerEvent());
        }
        return Optional.empty();
    }

    /**
     * Gets the internal selection key wrapped by this container.
     *
     * @return the internal selection key.
     */
    public SelectionKey getKey() {
        return key;
    }

    /**
     * Gets the internal selector that determined this network event, wrapped by
     * this container.
     *
     * @return the internal selector.
     */
    public Selector getSelector() {
        return selector;
    }

    /**
     * Gets the internal channel that the server is listening on, wrapped by
     * this container.
     *
     * @return the internal channel.
     */
    public ServerSocketChannel getChannel() {
        return channel;
    }
}
