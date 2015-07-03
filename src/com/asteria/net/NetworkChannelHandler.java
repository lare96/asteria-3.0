package com.asteria.net;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import com.asteria.game.World;
import com.asteria.net.message.Message;
import com.google.common.base.Objects;

/**
 * The {@link SimpleChannelInboundHandler} implementation assigned to all
 * registered channels that handles all upstream {@link Message}s from Netty.
 *
 * @author lare96 <http://github.com/lare96>
 */
@Sharable
public final class NetworkChannelHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
        if (!NetworkConstants.IGNORED_EXCEPTIONS.stream().anyMatch($it -> Objects.equal($it, e.getMessage())))
            e.printStackTrace();
        ctx.channel().close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE)
                ctx.channel().close();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        PlayerIO session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
        if (session == null)
            throw new IllegalStateException("session == null");
        World.queueLogout(session.getPlayer());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        try {
            PlayerIO session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
            if (session == null)
                throw new IllegalStateException("session == null");
            session.handleIncomingMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
