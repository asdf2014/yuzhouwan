package com.yuzhouwan.site.service.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：NIO Server
 *
 * @author Benedict Jin
 * @since 2016/9/1
 */
class NIOServer {

    /**
     * 原理参考：《Netty权威指南, 2nd》第22章 高性能之道
     */
    private static final Logger _log = LoggerFactory.getLogger(NIOServer.class);

    private static final int SOCKET_PORT = 6603;
    private static final Charset CHARSET = Charset.forName("UTF-8");

    private static SelectorLoop readBell;
    private boolean isReadBellRunning;

    // 启动服务器
    void startServer() throws IOException {
        // 准备好一个选择器, 监控是否有链接 (OP_ACCEPT)
        SelectorLoop connectionBell = new SelectorLoop();

        // 准备好一个选择器, 监控是否有read事件 (OP_READ)
        readBell = new SelectorLoop();

        // 开启一个server channel来监听
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 开启非阻塞模式
        ssc.configureBlocking(false);

        ServerSocket socket = ssc.socket();
        socket.bind(new InetSocketAddress("localhost", SOCKET_PORT));

        // 给选择器规定好要监听报告的事件, 这个选择器只监听新连接事件
        ssc.register(connectionBell.getSelector(), SelectionKey.OP_ACCEPT);
        new Thread(connectionBell).start();
    }

    // Selector轮询线程类
    private class SelectorLoop implements Runnable {

        private Selector selector;
        private ByteBuffer temp = ByteBuffer.allocate(1024);

        SelectorLoop() throws IOException {
            this.selector = Selector.open();
        }

        Selector getSelector() {
            return this.selector;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // 阻塞, 只有当至少一个注册的事件发生的时候才会继续
                    this.selector.select();

                    Set<SelectionKey> selectKeys = this.selector.selectedKeys();
                    Iterator<SelectionKey> it = selectKeys.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();
                        // 处理事件, 可以用多线程来处理
                        this.dispatch(key);
                    }
                } catch (IOException | InterruptedException e) {
                    _log.error("Selector has error: {}", e.getMessage());
                    break;
                }
            }
        }

        void dispatch(SelectionKey key) throws IOException, InterruptedException {
            if (key.isAcceptable()) {
                // 这是一个connection accept事件, 并且这个事件是注册在serverSocketChannel上的
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                // 接受一个连接
                SocketChannel sc = ssc.accept();

                // 对新的连接的channel注册read事件, 使用readBell
                sc.configureBlocking(false);
                sc.register(readBell.getSelector(), SelectionKey.OP_READ);

                // 如果读取线程还没有启动, 那就启动一个读取线程
                if (!NIOServer.this.isReadBellRunning) {
                    synchronized (NIOServer.this) {
                        if (!NIOServer.this.isReadBellRunning) {
                            NIOServer.this.isReadBellRunning = true;
                            new Thread(readBell).start();
                        }
                    }
                }

            } else if (key.isReadable()) {
                // 这是一个read事件, 并且这个事件是注册在socketChannel上的
                SocketChannel sc = (SocketChannel) key.channel();
                // 写数据到buffer
                int count = sc.read(temp);
                if (count < 0) {
                    // 客户端已经断开连接
                    key.cancel();
                    sc.close();
                    return;
                }
                // 切换buffer到读状态, 内部指针归位
                temp.flip();
                String msg = CHARSET.decode(temp).toString();
                _log.info("Server received [{}] from client address: {}", msg, sc.getRemoteAddress());

                Thread.sleep(5);
                // echo back
                sc.write(ByteBuffer.wrap(msg.getBytes(CHARSET)));

                // 清空buffer
                temp.clear();
            }
        }
    }
}