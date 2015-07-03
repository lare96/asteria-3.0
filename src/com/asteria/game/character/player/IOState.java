package com.asteria.game.character.player;

/**
 * The enumerated type whose elements represent the IO session states.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum IOState {

    /**
     * The client has just connected, is awaiting login protocol decoding.
     */
    CONNECTED,

    /**
     * The client is currently decoding the login protocol.
     */
    LOGGING_IN,

    /**
     * The client is now a player, and is logged in.
     */
    LOGGED_IN,
    
    /**
     * A request has been sent to disconnect the client.
     */
    LOGGING_OUT,

    /**
     * The client has disconnected from the server.
     */
    LOGGED_OUT
}