package com.asteria.game.character.player.login;

import java.util.ArrayList;
import java.util.List;

/**
 * The decoder chain that holds a series of events to decode the login protocol.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public final class LoginProtocolDecoderChain {

    /**
     * The chain that will hold the protocol decoder events.
     */
    private final List<LoginProtocolDecoder> chain = new ArrayList<>();

    /**
     * The size of the protocol decoder chain of events.
     */
    private final int size;

    /**
     * The current event this chain is on.
     */
    private int cursor;

    /**
     * Creates a new {@link LoginProtocolDecoderChain}.
     * 
     * @param size
     *            the size of the chain that will hold the events.
     */
    public LoginProtocolDecoderChain(int size) {
        this.size = size;
    }

    /**
     * Appends the {@code event} to the end of this protocol decoder chain.
     * 
     * @param event
     *            the event that will be appended to this chain.
     * @return an instance of this protocol decoder chain.
     */
    public LoginProtocolDecoderChain append(LoginProtocolDecoder event) {
        if (chain.size() == size)
            return this;
        chain.add(event);
        return this;
    }

    /**
     * The method that executes the protocol event on this index and then
     * increments the cursor.
     * 
     * @return an instance of this protocol decoder chain.
     */
    public LoginProtocolDecoderChain next() {
        chain.get(cursor++).run();
        return this;
    }

    /**
     * The method that executes the protocol event on this index and then
     * decrements the cursor.
     * 
     * @return an instance of this protocol decoder chain.
     */
    public LoginProtocolDecoderChain previous() {
        chain.get(cursor--).run();
        return this;
    }

    /**
     * Determines if this login protocol decoder has more events in its chain.
     * 
     * @return {@code true} if this decoder has more events, {@code false}
     *         otherwise.
     */
    public boolean hasNext() {
        return cursor < size;
    }
}
