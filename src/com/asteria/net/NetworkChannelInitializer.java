package com.asteria.net;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import com.asteria.net.login.LoginHandshakeHandler;

/**
 * The {@link ChannelInitializer} implementation that will initialize the
 * pipeline for newly registered {@link SocketChannel}s.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class NetworkChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * The {@link ChannelInboundHandlerAdapter} that will handle all upstream
     * message events from Netty.
     */
    private final ChannelInboundHandlerAdapter channelHandler = new NetworkChannelHandler();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        // Initialize our session Object when the channel is initialized, attach
        // it to the channel.
        ch.attr(NetworkConstants.SESSION_KEY).setIfAbsent(new PlayerIO(ch));

        // Initialize the pipeline channel handlers.
        ChannelDuplexHandler timeout = new IdleStateHandler(NetworkConstants.INPUT_TIMEOUT, 0, 0);
        ByteToMessageDecoder loginHandshakeHandler = new LoginHandshakeHandler();

        ch.pipeline().addLast("login-handshake", loginHandshakeHandler);
        ch.pipeline().addLast("channel-handler", channelHandler);
        ch.pipeline().addLast("timeout", timeout);
    }
}
