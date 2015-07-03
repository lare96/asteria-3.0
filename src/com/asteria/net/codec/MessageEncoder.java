package com.asteria.net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.asteria.net.ISAACCipher;
import com.asteria.net.message.MessageBuilder;

/**
 * The {@link MessageToByteEncoder} implementation that encodes and queues the
 * game logic for all outgoing {@link MessageBuilder}s.
 * 
 * @author lare96 <http://github.org/lare96>
 */
public final class MessageEncoder extends MessageToByteEncoder<MessageBuilder> {

    /**
     * The ISAAC that will encrypt outgoing messages.
     */
    private final ISAACCipher encryptor;

    /**
     * Creates a new {@link MessageEncoder}.
     *
     * @param encryptor
     *            the ISAAC that will encrypt outgoing messages.
     */
    public MessageEncoder(ISAACCipher encryptor) {
        this.encryptor = encryptor;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageBuilder msg, ByteBuf out) throws Exception {

        // Generate a new encryption key using ISAAC, encode the message and
        // write it to the client.
        ByteBuf internal = msg.buffer();
        int value = internal.getByte(0) + encryptor.getKey();
        internal.setByte(0, value);
        out.writeBytes(internal);
    }
}
