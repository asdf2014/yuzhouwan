package com.yuzhouwan.hacker.nio;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：Echo Client
 *
 * @author Benedict Jin
 * @since 2018/11/11
 */
public class EchoClient {

    private final String hostIp;
    private final int port;

    private EchoClient(String hostIp, int port) {
        this.hostIp = hostIp;
        this.port = port;
    }

    public void start() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(hostIp, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect().sync();
            future.addListener((ChannelFutureListener) listener -> {
                if (listener.isSuccess()) {
                    System.out.println("Client connected.");
                } else {
                    System.out.println("Server attempt failed!");
                    listener.cause().printStackTrace();
                }
            });
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new EchoClient("localhost", 16666).start();
    }
}
