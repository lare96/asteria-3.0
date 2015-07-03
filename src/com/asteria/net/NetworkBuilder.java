package com.asteria.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;

import java.io.IOException;
import java.util.logging.Logger;

import com.asteria.Server;
import com.asteria.utility.LoggerUtils;

/**
 * The network builder for the Runescape #317 protocol. This class is used to
 * start and configure the {@link ServerBootstrap} that will control and manage
 * the entire network.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class NetworkBuilder {

    /**
     * The logger that will print important information.
     */
    private final Logger logger = LoggerUtils.getLogger(NetworkBuilder.class);

    /**
     * The bootstrap that will oversee the management of the entire network.
     */
    private final ServerBootstrap bootstrap = new ServerBootstrap();

    /**
     * The event loop group that will be attached to the bootstrap.
     */
    private final EventLoopGroup loopGroup = new NioEventLoopGroup();

    /**
     * The {@link ChannelInitializer} that will determine how channels will be
     * initialized when registered to the event loop group.
     */
    private final ChannelInitializer<SocketChannel> channelInitializer = new NetworkChannelInitializer();

    /**
     * Initializes this network handler effectively preparing the server to
     * listen for connections and handle network events.
     *
     * @param port
     *            the port that this network will be bound to.
     * @throws Exception
     *             if any issues occur while starting the network.
     */
    public void initialize(int port) throws IOException {
        if (port != 43594 && port != 5555 && port != 43595)
            logger.warning("The preferred ports for Runescape servers are 43594, 5555, and 43595!");
        ResourceLeakDetector.setLevel(Server.DEBUG ? Level.PARANOID : NetworkConstants.RESOURCE_DETECTION);
        bootstrap.group(loopGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(channelInitializer);
        bootstrap.bind(port).syncUninterruptibly();
    }
}
