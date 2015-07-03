package com.asteria.net.login;

import io.netty.channel.Channel;

/**
 * The {@link RuntimeException} implementation that is thrown whenever an
 * unexpected error occurs during the login protocol.
 * 
 * @author lare96 <http://github.org/lare96>
 */
public final class InvalidLoginException extends RuntimeException {

    /**
     * The serial version UID for serialization.
     */
    private static final long serialVersionUID = -8267882623161271111L;

    /**
     * Creates a new {@link InvalidLoginException}.
     *
     * @param channel
     *            the channel that was performing the login.
     * @param message
     *            the issue that occurred with the login.
     */
    public InvalidLoginException(Channel channel, String message) {
        super(message);
        channel.close();
    }
}
