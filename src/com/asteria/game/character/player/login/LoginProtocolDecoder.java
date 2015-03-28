package com.asteria.game.character.player.login;

import com.asteria.game.character.player.IOState;
import com.asteria.game.character.player.PlayerIO;

/**
 * The parent class that provides functions for decoding the login protocol.
 *
 * @author lare96 <http://github.com/lare96>
 */
public abstract class LoginProtocolDecoder implements Runnable {

    /**
     * The session that is decoding this protocol.
     */
    private final PlayerIO session;

    /**
     * The IO state the session must have to proceed.
     */
    private final IOState state;

    /**
     * Creates a new {@link LoginProtocolDecoder}.
     *
     * @param session
     *         the session that is decoding this protocol.
     * @param state
     *         the IO state the session must have to proceed.
     */
    public LoginProtocolDecoder(PlayerIO session, IOState state) {
        this.session = session;
        this.state = state;
    }

    @Override
    public final void run() {
        try {
            if (session.getState() != state) {
                session.disconnect(false);
                return;
            }
            execute();
        } catch (Exception e) {
            e.printStackTrace();
            session.disconnect(false);
        }
    }

    /**
     * The method that will decode the login protocol.
     */
    public abstract void execute();

    /**
     * Gets the session that is decoding this protocol.
     *
     * @return the session decoding the protocol.
     */
    public final PlayerIO getSession() {
        return session;
    }
}
