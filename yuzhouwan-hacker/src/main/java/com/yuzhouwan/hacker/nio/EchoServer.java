package com.yuzhouwan.hacker.nio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：Echo Server
 *
 * @author Benedict Jin
 * @since 2018/11/11
 */
public class EchoServer {

    private final static int port = 16666;

    public static void main(String[] args) throws InterruptedException {
        new EchoServer().start();
    }

    public void start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap(); // 引导辅助程序
        EventLoopGroup group = new NioEventLoopGroup(); // 通过 nio 的方式接受连接和处理连接
        try {
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class) //设置 nio 类型的 channel
                    .localAddress(new InetSocketAddress(port)) // 设置监听端口
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 有连接到达时会创建一个 channel
                        // pipeline 管理 channel 中的 handler,在 channel 队列中添加一个 handler 来处理业务
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast("myHandler", new EchoServerHandler());
                            // ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(0, 0, 180));
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync(); //配置完成，绑定 server，并通过 sync 同步方法阻塞直到绑定成功
            System.out.println(EchoServer.class.getName() + " started and listen on " + future.channel().localAddress());
            future.channel().closeFuture().sync(); // 应用程序会一直等待，直到 channel 关闭
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
