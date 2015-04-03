package com.asteria.game.character.player.login.impl;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import com.asteria.game.World;
import com.asteria.game.character.player.IOState;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.PlayerIO;
import com.asteria.game.character.player.login.LoginProtocolDecoder;
import com.asteria.game.character.player.login.LoginResponse;
import com.asteria.game.character.player.serialize.PlayerSerialization;
import com.asteria.network.DataBuffer;
import com.asteria.network.ISAACCipher;
import com.asteria.utility.LoggerUtils;
import com.asteria.utility.Settings;
import com.asteria.utility.TextUtils;

/**
 * The login protocol decoder that handles the rest of the login session. This
 * marks the ending of the entire login protocol in one of two stages.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class PostHandshakeLoginDecoder extends LoginProtocolDecoder {

    /**
     * The logger that will print important information.
     */
    private static Logger logger = LoggerUtils.getLogger(PlayerIO.class);

    /**
     * Creates a new {@link PostHandshakeLoginDecoder}.
     *
     * @param session
     *         the session that is decoding this protocol.
     */
    public PostHandshakeLoginDecoder(PlayerIO session) {
        super(session, IOState.LOGGING_IN);
    }

    @Override
    public void execute() {
        PlayerIO session = getSession();
        ByteBuffer inData = session.getInData();
        if (inData.remaining() < 2) {
            inData.compact();
            return;
        }
        int loginType = inData.get();
        if (loginType != 16 && loginType != 18) {
            logger.warning(session + " invalid login type!");
            session.disconnect(false);
            return;
        }
        int blockLength = inData.get() & 0xff;
        int loginEncryptPacketSize = blockLength - (36 + 1 + 1 + 2);
        if (loginEncryptPacketSize <= 0) {
            logger.warning(session + " invalid RSA packet size!");
            session.disconnect(false);
            return;
        }
        if (inData.remaining() < blockLength) {
            inData.flip();
            inData.compact();
            return;
        }
        DataBuffer in = DataBuffer.create(inData);
        in.get();
        int clientVersion = in.getShort();
        if (clientVersion != 317) {
            logger.warning(session + " invalid client version");
            session.disconnect(false);
            return;
        }
        in.get();
        for (int i = 0; i < 9; i++)
            in.getInt();
        loginEncryptPacketSize--;
        in.get();
        String username = null;
        String password = null;
        if (Settings.DECODE_RSA) {
            byte[] encryptionBytes = new byte[loginEncryptPacketSize];
            in.buffer().get(encryptionBytes);
            ByteBuffer rsaBuffer = ByteBuffer.wrap(new BigInteger(encryptionBytes).modPow(Settings.RSA_EXPONENT, Settings.RSA_MODULUS).toByteArray());
            int rsaOpcode = rsaBuffer.get();
            if (rsaOpcode != 10) {
                logger.warning(session + " unable to decode RSA block " +
                        "properly!");
                session.disconnect(false);
                return;
            }
            long clientHalf = rsaBuffer.getLong();
            long serverHalf = rsaBuffer.getLong();
            int[] isaacSeed = {(int) (clientHalf >> 32), (int) clientHalf,
                    (int) (serverHalf >> 32), (int) serverHalf};
            session.setDecryptor(new ISAACCipher(isaacSeed));
            for (int i = 0; i < isaacSeed.length; i++)
                isaacSeed[i] += 50;
            session.setEncryptor(new ISAACCipher(isaacSeed));
            rsaBuffer.getInt();
            DataBuffer readStr = DataBuffer.create(rsaBuffer);
            username = readStr.getString();
            password = readStr.getString();
        } else {
            in.buffer().get();
            long clientHalf = in.buffer().getLong();
            long serverHalf = in.buffer().getLong();
            int[] isaacSeed = {(int) (clientHalf >> 32), (int) clientHalf,
                    (int) (serverHalf >> 32), (int) serverHalf};
            session.setDecryptor(new ISAACCipher(isaacSeed));
            for (int i = 0; i < isaacSeed.length; i++)
                isaacSeed[i] += 50;
            session.setEncryptor(new ISAACCipher(isaacSeed));
            in.buffer().getInt();
            username = in.getString();
            password = in.getString();
        }
        username = username.toLowerCase().replaceAll("_", " ").trim();
        password = password.toLowerCase();
        boolean invalidCredentials = !username.matches("^[a-zA-Z0-9_ ]{1," + "12}$") || password.isEmpty() || password.length() > 20;
        session.setResponse(invalidCredentials ? LoginResponse.INVALID_CREDENTIALS : World.getPlayers().spaceLeft() == 0 ? LoginResponse.WORLD_FULL : session.getResponse());
        Player player = session.getPlayer();
        if (session.getResponse() == LoginResponse.NORMAL) {
            player.setUsername(username);
            player.setUsernameHash(TextUtils.nameToHash(username));
            player.setPassword(password);
            if (World.getPlayer(player.getUsernameHash()).isPresent()) {
                session.setResponse(LoginResponse.ACCOUNT_ONLINE);
            }
            if (session.getResponse() == LoginResponse.NORMAL) {
                session.setResponse(new PlayerSerialization(player).deserialize(password));
            }
        }
        DataBuffer resp = DataBuffer.create(3);
        resp.put(session.getResponse().getCode());
        resp.put(player.getRights().getProtocolValue());
        resp.put(0);
        session.send(resp);
        if (session.getResponse() != LoginResponse.NORMAL) {
            session.disconnect(false);
            return;
        }
        World.getPlayers().add(player);
    }
}
