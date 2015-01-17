package com.asteria.game.character.player.login;

import com.asteria.game.character.player.IOState;
import com.asteria.game.character.player.PlayerIO;

/**
 * The parent class that provides functions for decoding the login protocol.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public abstract class LoginProtocolDecoder implements Runnable {

    /**
     * The session that is decoding this protocol.
     */
    private final PlayerIO session;

    /**
     * Creates a new {@link LoginProtocolDecoder}.
     * 
     * @param session
     *            the session that is decoding this protocol.
     */
    public LoginProtocolDecoder(PlayerIO session) {
        this.session = session;
    }

    @Override
    public final void run() {
        try {
            if (session.getState() != state()) {
                session.disconnect();
                return;
            }
            execute();
        } catch (Exception e) {
            e.printStackTrace();
            session.disconnect();
        }
    }

    /**
     * The method that will decode the login protocol.
     */
    public abstract void execute();

    /**
     * The I/O state the session must have to proceed.
     * 
     * @return the required I/O state.
     */
    public abstract IOState state();

    /**
     * Gets the session that is decoding this protocol.
     * 
     * @return the session decoding the protocol.
     */
    public final PlayerIO getSession() {
        return session;
    }
}
