package com.asteria.game.character.player.login.impl;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.logging.Logger;

import com.asteria.game.character.player.IOState;
import com.asteria.game.character.player.PlayerIO;
import com.asteria.game.character.player.login.LoginProtocolDecoder;
import com.asteria.network.DataBuffer;
import com.asteria.utility.LoggerUtils;

/**
 * The login protocol decoder that handles the handshake between the client and
 * the server. This marks the beginning of the entire login protocol in one of
 * two stages.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class HandshakeLoginDecoder extends LoginProtocolDecoder {

    /**
     * The logger that will print important information.
     */
    private final Logger logger = LoggerUtils.getLogger(PlayerIO.class);

    /**
     * Creates a new {@link HandshakeLoginDecoder}.
     *
     * @param session
     *            the session that is decoding this protocol.
     */
    public HandshakeLoginDecoder(PlayerIO session) {
        super(session, IOState.CONNECTED);
    }

    @Override
    public void execute() {
        PlayerIO session = getSession();
        ByteBuffer in = session.getInData();
        if (in.remaining() < 2) {
            in.compact();
            return;
        }
        int request = in.get() & 0xff;
        in.get();
        if (request != 14) {
            logger.warning("Invalid login request: " + request);
            session.disconnect(false);
            return;
        }
        DataBuffer out = DataBuffer.create(17);
        out.putLong(0);
        out.put(0);
        out.putLong(new SecureRandom().nextLong());
        session.send(out);
        session.setState(IOState.LOGGING_IN);
    }
}
