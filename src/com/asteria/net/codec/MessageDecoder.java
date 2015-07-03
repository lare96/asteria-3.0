package com.asteria.net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.logging.Logger;

import com.asteria.Server;
import com.asteria.net.ISAACCipher;
import com.asteria.net.NetworkConstants;
import com.asteria.net.PlayerIO;
import com.asteria.net.message.InputMessage;
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

        // Determine the opcode, size, if the message has a listener, and
        // retrieve the session attachment from the attribute map.
        if (!in.isReadable()) {
            ctx.channel().close();
            return;
        }
        PlayerIO session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
        int opcode = -1;
        int size = -1;
        boolean hasMessage = false;
        opcode = in.readUnsignedByte();
        opcode = (opcode - decryptor.getKey()) & 0xFF;
        size = NetworkConstants.MESSAGE_SIZES[opcode];
        hasMessage = NetworkConstants.MESSAGES[opcode] != null;

        // The message has no payload to decode, so queue it over to be received
        // upstream by the channel handler.
        if (size == 0) {
            if (hasMessage) {

                // EMPTY_BUFFER because this message has no payload.
                out.add(new InputMessage(opcode, size, MessageBuilder.create(Unpooled.EMPTY_BUFFER)));
            } else {
                in.readBytes(new byte[size]);
                if (Server.DEBUG)
                    logger.info(session + " unhandled upstream message [opcode= " + opcode + ", size= " + size + "]");
            }
            return;
        }

        // The message is variable sized or variable short sized, so determine
        // the exact size of the message.
        if (size == -1 || size == -2) {
            int bytes = size == -1 ? Byte.BYTES : Short.BYTES;
            if (!in.isReadable(bytes)) {
                ctx.channel().close();
                return;
            }
            size = 0;
            for (int i = 0; i < bytes; i++) {
                size |= in.readUnsignedByte() << 8 * (bytes - 1 - i);
            }
        }

        // Finally, here we wrap the payload in our custom wrapper buffer
        // designed to decode data from the Runescape client. We then queue it
        // over to be received upstream by the channel handler.
        if (!in.isReadable(size)) {
            ctx.channel().close();
            return;
        }
        if (hasMessage) {
            ByteBuf buffer = in.readBytes(size);
            out.add(new InputMessage(opcode, size, MessageBuilder.create(buffer)));
        } else {
            in.readBytes(new byte[size]);
            if (Server.DEBUG)
                logger.info(session + " unhandled upstream message [opcode= " + opcode + ", size= " + size + "]");
        }
    }
}
