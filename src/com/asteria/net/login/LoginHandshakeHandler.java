package com.asteria.net.login;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.security.SecureRandom;
import java.util.List;

import com.asteria.game.character.player.IOState;
import com.asteria.net.NetworkConstants;
import com.asteria.net.PlayerIO;

/**
 * The {@link ByteToMessageDecoder} implementation that will manage the
 * handshake section of the login protocol.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class LoginHandshakeHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        // Read the initial request value, validate it.
        SecureRandom random = new SecureRandom();
        if (in.readableBytes() < 2)
            return;

        int request = in.readUnsignedByte();
        in.readByte();
        if (request != 14)
            throw new InvalidLoginException(ctx.channel(), "Invalid login request [" + request + "]");

        // Write and send the response to the request.
        ByteBuf buf = Unpooled.buffer(17);
        buf.writeLong(0);
        buf.writeByte(0);
        buf.writeLong(random.nextLong());
        ctx.channel().writeAndFlush(buf);

        // Reconfigure the pipeline and session state for the next login stage.
        PlayerIO session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
        session.setState(IOState.LOGGING_IN);
        ctx.pipeline().addAfter("login-handshake", "post-login-handshake", new PostLoginHandshakeHandler());
        ctx.pipeline().remove(this);
    }
}