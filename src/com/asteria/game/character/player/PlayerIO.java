package com.asteria.game.character.player;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import plugin.minigames.fightcaves.FightCavesHandler;

import com.asteria.game.World;
import com.asteria.game.character.player.login.LoginProtocolDecoderChain;
import com.asteria.game.character.player.login.LoginResponse;
import com.asteria.game.character.player.login.impl.HandshakeLoginDecoder;
import com.asteria.game.character.player.login.impl.PostHandshakeLoginDecoder;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.location.Position;
import com.asteria.game.shop.Shop;
import com.asteria.network.ConnectionHandler;
import com.asteria.network.DataBuffer;
import com.asteria.network.ISAACCipher;
import com.asteria.task.TaskHandler;
import com.asteria.utility.LoggerUtils;
import com.asteria.utility.MutableNumber;
import com.asteria.utility.Stopwatch;

/**
 * The session handler dedicated to a player that will handle input and output
 * operations.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 * @author blakeman8192
 */
public final class PlayerIO {

    /**
     * The logger that will print important information.
     */
    private static Logger logger = LoggerUtils.getLogger(PlayerIO.class);

    /**
     * The byte buffer for reading data from the client.
     */
    private final ByteBuffer inData = ByteBuffer.allocateDirect(512);

    /**
     * The byte buffer for writing data to the client.
     */
    private final ByteBuffer outData = ByteBuffer.allocateDirect(8192);

    /**
     * The selection key registered to the selector.
     */
    private final SelectionKey key;

    /**
     * The socket channel for sending and receiving raw data.
     */
    private final SocketChannel channel;

    /**
     * The player I/O operations will be executed for.
     */
    private final Player player;

    /**
     * The host address this session is bound to.
     */
    private final String host;

    /**
     * The login protocol decoder chain of events.
     */
    private final LoginProtocolDecoderChain chain;

    /**
     * The stopwatch that determines when this I/O session will timeout.
     */
    private final Stopwatch timeout = new Stopwatch();

    /**
     * The amount of packets that have been decoded this sequence.
     */
    private final MutableNumber packetCount = new MutableNumber();

    /**
     * The current state of this I/O session.
     */
    private IOState state = IOState.CONNECTED;

    /**
     * The current login response for this session.
     */
    private LoginResponse response;

    /**
     * The opcode of the packet currently being decoded.
     */
    private int packetOpcode = -1;

    /**
     * The size of the packet currently being decoded.
     */
    private int packetSize = -1;

    /**
     * The encryptor that will encode sent packets.
     */
    private ISAACCipher encryptor;

    /**
     * The decryptor that will decode received packets.
     */
    private ISAACCipher decryptor;

    /**
     * The flag that determines if the player disconnected while sending data.
     */
    private boolean packetDisconnect;

    /**
     * Creates a new {@link PlayerIO}.
     * 
     * @param key
     *            the selection key registered to the selector.
     * @param response
     *            the current login response for this session.
     */
    public PlayerIO(SelectionKey key, LoginResponse response) {
        this.key = key;
        this.response = response;
        this.channel = (SocketChannel) key.channel();
        this.host = channel.socket().getInetAddress().getHostAddress().toLowerCase();
        this.player = new Player(this);
        this.chain = new LoginProtocolDecoderChain(2).append(new HandshakeLoginDecoder(this)).append(
            new PostHandshakeLoginDecoder(this));
    }

    @Override
    public String toString() {
        return "SESSION[host= " + host + ", state= " + state.name() + "]";
    }

    /**
     * Disconnects this session from the server by canceling the registered key
     * and closing the socket channel.
     * 
     * @param forced
     *            if the session must be disconnected because of an IO issue.
     */
    public void disconnect(boolean forced) {
        try {
            packetDisconnect = forced;
            if (state == IOState.LOGGED_IN) {
                if (player.getOpenShop() != null)
                    Shop.SHOPS.get(player.getOpenShop()).getPlayers().remove(player);
                TaskHandler.cancel(player.getCombatBuilder());
                TaskHandler.cancel(player);
                player.setSkillAction(false);
                World.getPlayers().remove(player);
                MinigameHandler.execute(player, m -> m.onLogout(player));
                player.getTradeSession().reset(false);
                player.getPrivateMessage().updateOtherList(false);
                if (FightCavesHandler.remove(player))
                    player.move(new Position(2399, 5177));
                player.save();
            }
            key.attach(null);
            key.cancel();
            channel.close();
            ConnectionHandler.remove(host);
            logger.info(state == IOState.LOGGED_IN ? player + " has logged out." : this + " has logged out.");
            state = IOState.LOGGED_OUT;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a packet of data to the client through {@code buffer}.
     * 
     * @param buffer
     *            the packet of data to send.
     */
    public void send(ByteBuffer buffer) {
        if (!channel.isOpen() || packetDisconnect)
            return;
        buffer.flip();
        try {
            channel.write(buffer);
        } catch (Exception ex) {
            ex.printStackTrace();
            disconnect(true);
        }
    }

    /**
     * Sends a packet of data to the client through {@code buffer}.
     * 
     * @param buffer
     *            the packet of data to send.
     */
    public void send(DataBuffer buffer) {
        send(buffer.buffer());
    }

    /**
     * Gets the byte buffer for reading data from the client.
     * 
     * @return the buffer for reading.
     */
    public ByteBuffer getInData() {
        return inData;
    }

    /**
     * Gets the byte buffer for writing data to the client.
     * 
     * @return the buffer for writing.
     */
    public ByteBuffer getOutData() {
        return outData;
    }

    /**
     * Gets the selection key registered to the selector.
     * 
     * @return the selection key.
     */
    public SelectionKey getKey() {
        return key;
    }

    /**
     * Gets the socket channel for sending and receiving raw data.
     * 
     * @return the socket channel.
     */
    public SocketChannel getChannel() {
        return channel;
    }

    /**
     * Gets the player I/O operations will be executed for.
     * 
     * @return the player I/O operations.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the host address this session is bound to.
     * 
     * @return the host address.
     */
    public String getHost() {
        return host;
    }

    /**
     * Gets the login protocol decoder chain of events.
     * 
     * @return the chain of events.
     */
    public LoginProtocolDecoderChain getChain() {
        return chain;
    }

    /**
     * Gets the stopwatch that determines when this I/O session will timeout.
     * 
     * @return the stopwatch for determining timeout.
     */
    public Stopwatch getTimeout() {
        return timeout;
    }

    /**
     * Gets the amount of packets that have been decoded this sequence.
     * 
     * @return the amount of packets decoded.
     */
    public MutableNumber getPacketCount() {
        return packetCount;
    }

    /**
     * Gets the current state of this I/O session.
     * 
     * @return the current state.
     */
    public IOState getState() {
        return state;
    }

    /**
     * Sets the value for {@link PlayerIO#state}.
     * 
     * @param state
     *            the new value to set.
     */
    public void setState(IOState state) {
        this.state = state;
    }

    /**
     * Gets the current login response for this session.
     * 
     * @return the current login response.
     */
    public LoginResponse getResponse() {
        return response;
    }

    /**
     * Sets the value for {@link PlayerIO#response}.
     * 
     * @param response
     *            the new value to set.
     */
    public void setResponse(LoginResponse response) {
        this.response = response;
    }

    /**
     * Gets the opcode of the packet currently being decoded.
     * 
     * @return the opcode of the packet.
     */
    public int getPacketOpcode() {
        return packetOpcode;
    }

    /**
     * Sets the value for {@link PlayerIO#packetOpcode}.
     * 
     * @param packetOpcode
     *            the new value to set.
     */
    public void setPacketOpcode(int packetOpcode) {
        this.packetOpcode = packetOpcode;
    }

    /**
     * Gets the size of the packet currently being decoded.
     * 
     * @return the size of the packet.
     */
    public int getPacketSize() {
        return packetSize;
    }

    /**
     * Sets the value for {@link PlayerIO#packetSize}.
     * 
     * @param packetSize
     *            the new value to set.
     */
    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }

    /**
     * Gets the encryptor that will encode sent packets.
     * 
     * @return the encryptor.
     */
    public ISAACCipher getEncryptor() {
        return encryptor;
    }

    /**
     * Sets the value for {@link PlayerIO#encryptor}.
     * 
     * @param encryptor
     *            the new value to set.
     */
    public void setEncryptor(ISAACCipher encryptor) {
        this.encryptor = encryptor;
    }

    /**
     * Gets the decryptor that will decode received packets.
     * 
     * @return the decryptor.
     */
    public ISAACCipher getDecryptor() {
        return decryptor;
    }

    /**
     * Sets the value for {@link PlayerIO#decryptor}.
     * 
     * @param decryptor
     *            the new value to set.
     */
    public void setDecryptor(ISAACCipher decryptor) {
        this.decryptor = decryptor;
    }
}
