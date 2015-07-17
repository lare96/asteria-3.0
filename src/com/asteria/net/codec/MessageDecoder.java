package com.asteria.net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.asteria.Server;
import com.asteria.net.ISAACCipher;
import com.asteria.net.NetworkConstants;
import com.asteria.net.PlayerIO;
import com.asteria.net.message.InputMessage;
import com.asteria.net.message.InputMessageListener;
import com.asteria.net.message.MessageBuilder;
import com.asteria.utility.LoggerUtils;

/**
 * The {@link ByteToMessageDecoder} implementation that decodes and queues the
 * game logic for all incoming {@link InputMessage}s.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class MessageDecoder extends ByteToMessageDecoder {

    /**
     * The logger that will print important information.
     */
    private static Logger logger = LoggerUtils.getLogger(MessageDecoder.class);

    /**
     * The ISAAC that will decrypt incoming messages.
     */
    private final ISAACCipher decryptor;

    /**
     * The state of the message being decoded.
     */
    private State state = State.OPCODE;

    /**
     * The opcode of the message being decoded.
     */
    private int opcode;

    /**
     * The size of the message being decoded.
     */
    private int size;

    /**
     * Creates a new {@link MessageDecoder}.
     *
     * @param decryptor
     *            the ISAAC decryptor that decodes data.
     */
    public MessageDecoder(ISAACCipher decryptor) {
        this.decryptor = decryptor;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state) {
        case OPCODE:
            opcode(ctx, in).ifPresent(out::add);
            break;
        case SIZE:
            size(in);
            break;
        case PAYLOAD:
            payload(ctx, in).ifPresent(out::add);
            break;
        }
    }

    /**
     * Decode the message opcode and determine the message type. If the message
     * is variable sized set the next state to {@code SIZE}, otherwise set it to
     * {@code PAYLOAD}.
     * 
     * @param ctx
     *            the context for our channel, used to retrieve the session
     *            instance.
     * @param msg
     *            the message to decode the opcode from.
     * @return an optional containing a message with no payload, or an empty
     *         optional.
     */
    private Optional<InputMessage> opcode(ChannelHandlerContext ctx, ByteBuf msg) {
        if (msg.isReadable()) {
            opcode = msg.readUnsignedByte();
            opcode = (opcode - decryptor.getKey()) & 0xFF;
            size = NetworkConstants.MESSAGE_SIZES[opcode];
            if (size == 0)
                return message(ctx, Unpooled.EMPTY_BUFFER);
            state = size == NetworkConstants.VAR_SIZE || size == NetworkConstants.VAR_SIZE_SHORT ? State.SIZE : State.PAYLOAD;
        }
        return Optional.empty();
    }

    /**
     * Decode the message size for variable sized messages, then set the state
     * to {@code PAYLOAD}.
     * 
     * @param msg
     *            the message to decode the size from.
     */
    private void size(ByteBuf msg) {
        int bytes = size == NetworkConstants.VAR_SIZE ? Byte.BYTES : Short.BYTES;
        if (msg.isReadable(bytes)) {
            size = 0;
            for (int i = 0; i < bytes; i++)
                size |= msg.readUnsignedByte() << 8 * (bytes - 1 - i);
            state = State.PAYLOAD;
        }
    }

    /**
     * Decode the payload for this message, then queue it over to be received
     * upstream by the Netty channel handler.
     * 
     * @param ctx
     *            the context for our channel, used to retrieve the session
     *            instance.
     * @param msg
     *            the message to decode the payload from.
     * @return an optional containing the successfully decoded
     */
    private Optional<InputMessage> payload(ChannelHandlerContext ctx, ByteBuf msg) {
        if (msg.isReadable(size))
            return message(ctx, msg.readBytes(size));
        return Optional.empty();
    }

    /**
     * Determines if an {@link InputMessageListener} is available for the
     * current opcode, if it is it returns a new {@code InputMessage} wrapped in
     * an optional, if not it returns an empty optional. Before this method
     * returns, the state is reset to {@code OPCODE} and the opcode and size
     * values are reset to {@code -1}.
     * 
     * 
     * @param ctx
     *            the context for our channel, used to retrieve the session
     *            instance.
     * @param payload
     *            the payload to pack in this message.
     * @return an optional containing the message if available, an empty
     *         optional otherwise.
     */
    private Optional<InputMessage> message(ChannelHandlerContext ctx, ByteBuf payload) {
        try {
            if (NetworkConstants.MESSAGES[opcode] != null)
                return Optional.of(new InputMessage(opcode, size, MessageBuilder.create(payload)));
            if (Server.DEBUG) {
                PlayerIO session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
                logger.info(session + " unhandled upstream message [opcode= " + opcode + ", size= " + size + "]");
            }
        } finally {
            state = State.OPCODE;
            opcode = -1;
            size = -1;
        }
        return Optional.empty();
    }

    /**
     * The enumerated type representing all of the possible states of this
     * decoder.
     * 
     * @author lare96 <http://github.org/lare96>
     */
    private enum State {
        OPCODE,
        SIZE,
        PAYLOAD
    }
}
