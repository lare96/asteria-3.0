package com.asteria.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.SocketChannel;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.asteria.game.World;
import com.asteria.game.character.player.IOState;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.Rights;
import com.asteria.game.character.player.serialize.PlayerSerialization;
import com.asteria.net.codec.MessageDecoder;
import com.asteria.net.codec.MessageEncoder;
import com.asteria.net.login.LoginResponse;
import com.asteria.net.message.InputMessage;
import com.asteria.net.message.InputMessageListener;
import com.asteria.net.message.LoginDetailsMessage;
import com.asteria.net.message.Message;
import com.asteria.net.message.MessageBuilder;
import com.asteria.utility.TextUtils;

/**
 * The session handler dedicated to a player that will handle input and output
 * operations.
 *
 * @author lare96 <http://github.com/lare96>
 * @author blakeman8192
 */
public final class PlayerIO {

    /**
     * The queue of messages that will be handled on the next sequence.
     */
    private final Queue<InputMessage> messageQueue = new ConcurrentLinkedQueue<>();

    /**
     * The channel that will manage the connection for this player.
     */
    private final Channel channel;

    /**
     * The player I/O operations will be executed for.
     */
    private final Player player;

    /**
     * The host address this session is bound to.
     */
    private final String host;

    /**
     * The current state of this I/O session.
     */
    private IOState state = IOState.CONNECTED;

    /**
     * The current login response for this session.
     */
    private LoginResponse response;

    /**
     * Creates a new {@link PlayerIO}.
     *
     * @param channel
     *            the socket channel that data will be written to.
     */
    public PlayerIO(SocketChannel channel) {
        this.host = channel.remoteAddress().getAddress().getHostAddress();
        this.response = ConnectionHandler.evaluate(host);
        this.channel = channel;
        this.player = new Player(this);
    }

    @Override
    public String toString() {
        return state == IOState.LOGGED_IN ? player.toString() : "SESSION[host= " + host + ", state= " + state + "]";
    }

    /**
     * Queues the {@code msg} for this session to be encoded and sent to the
     * client.
     *
     * @param msg
     *            the message to queue.
     */
    public void queue(MessageBuilder msg) {
        try {
            if (!channel.isOpen())
                return;
            channel.writeAndFlush(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
            channel.close();
        }
    }

    /**
     * Uses state-machine to handle upstream messages from Netty.
     * 
     * @param msg
     *            the message to handle.
     */
    public void handleIncomingMessage(Message msg) {
        switch (state) {

        // Handle the login details, send the final response to the client
        // before queuing the session over to the main game thread to be logged
        // in on the next sequence.
        case LOGGING_IN:
            if (msg instanceof LoginDetailsMessage)
                finalizeDetails((LoginDetailsMessage) msg);
            break;

        // We are already logged in, handle incoming messages from the client by
        // queuing them over to the main game thread to be processed on the next
        // sequence.
        case LOGGED_IN:
            if (msg instanceof InputMessage) {
                if (messageQueue.size() <= NetworkConstants.DECODE_LIMIT)
                    messageQueue.add((InputMessage) msg);
            }
            break;
        default:
            throw new IllegalStateException("Cannot receive upstream messages when " + state + ".");
        }
    }

    /**
     * Ensures that the login details are valid and completes the last part of
     * the login protocol by sending the final login response code.
     * 
     * @param msg
     *            the message containing the login details.
     */
    private void finalizeDetails(LoginDetailsMessage msg) {

        // Validate the username and password, change login response if needed
        // for invalid credentials or the world being full.
        boolean invalidCredentials = !msg.getUsername().matches("^[a-zA-Z0-9_ ]{1," + "12}$") || msg.getPassword().isEmpty() || msg
            .getPassword().length() > 20;
        response = invalidCredentials ? LoginResponse.INVALID_CREDENTIALS : World.getPlayers().spaceLeft() == 0 ? LoginResponse.WORLD_FULL
            : response;

        // If the login response is normal, deserialize the character file (or
        // grab it from the Cache if it was recently serialized).
        if (response == LoginResponse.NORMAL) {
            player.setUsername(msg.getUsername());
            player.setUsernameHash(TextUtils.nameToHash(msg.getUsername()));
            player.setPassword(msg.getPassword());
            if (World.getPlayer(player.getUsernameHash()).isPresent()) {
                response = LoginResponse.ACCOUNT_ONLINE;
            }
            if (response == LoginResponse.NORMAL) {
                response = new PlayerSerialization(player).deserialize(msg.getPassword());
            }
            player.setRights(ConnectionHandler.isLocal(host) ? Rights.DEVELOPER : player.getRights());
        }

        // Write the final response, send it off to the client.
        ByteBuf resp = Unpooled.buffer(3);
        resp.writeByte(response.getCode());
        resp.writeByte(player.getRights().getProtocolValue());
        resp.writeByte(0);

        // If the response was invalid, close the channel right after the data
        // is sent to the client.
        ChannelFuture future = msg.getCtx().channel().writeAndFlush(resp);
        if (response != LoginResponse.NORMAL) {
            future.addListener(ChannelFutureListener.CLOSE);
            return;
        }

        // Everything went well, so queue rearrange the pipeline for gameplay
        // and queue the player for login.
        msg.getCtx().pipeline().addAfter("post-login-handshake", "encoder", new MessageEncoder(msg.getEncryptor()));
        msg.getCtx().pipeline().addAfter("encoder", "decoder", new MessageDecoder(msg.getDecryptor()));
        msg.getCtx().pipeline().remove("post-login-handshake");
        World.queueLogin(player);
    }

    /**
     * Handles all of the queued messages from the {@link MessageDecoder} by
     * polling the internal queue.
     */
    public void handleQueuedMessages() {
        InputMessage msg;
        while ((msg = messageQueue.poll()) != null) {
            try {
                InputMessageListener listener = NetworkConstants.MESSAGES[msg.getOpcode()];
                listener.handleMessage(player, msg.getOpcode(), msg.getSize(), msg.getPayload());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the channel that will manage the connection for this player.
     *
     * @return the channel for this player.
     */
    public Channel getChannel() {
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
}
