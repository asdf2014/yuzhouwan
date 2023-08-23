package com.yuzhouwan.site.service.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：NIO Client
 *
 * @author Benedict Jin
 * @since 2016/9/1
 */
class NIOClient implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(NIOClient.class);

    private static final int SOCKET_PORT = 6603;
    private static final ByteBuffer temp = ByteBuffer.allocate(1024);
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final String msg = "Yuzhouwan";

    // 空闲计数器, 如果空闲超过10次, 将检测server是否中断连接
    private static int idleCounter;
    private final Selector selector;
    private final SocketChannel socketChannel;

    NIOClient() throws IOException {
        // 同样的, 注册选择器
        this.selector = Selector.open();

        // 连接远程server
        socketChannel = SocketChannel.open();
        // 如果快速的建立了连接, 返回true. 如果没有建立, 则返回false, 并在连接后出发Connect事件
        boolean isConnected = socketChannel.connect(new InetSocketAddress("localhost", SOCKET_PORT));
        socketChannel.configureBlocking(false);
        SelectionKey key = socketChannel.register(selector, SelectionKey.OP_READ);

        if (isConnected) {
            this.sendFirstMsg();
        } else {
            // 如果连接还在尝试中, 则注册connect事件的监听, connect成功以后会出发connect事件
            key.interestOps(SelectionKey.OP_CONNECT);
        }
    }

    private void sendFirstMsg() throws IOException {
        socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 阻塞, 等待事件发生, 或者1秒超时, num为发生事件的数量
                int num = this.selector.select(1000);
                if (num == 0) {
                    idleCounter++;
                    if (idleCounter > 10) {
                        // 如果server断开了连接, 发送消息将失败
                        try {
                            this.sendFirstMsg();
                        } catch (ClosedChannelException e) {
                            LOGGER.warn("Channel closed with {}!", e.getMessage());
                            this.socketChannel.close();
                            return;
                        }
                    }
                    continue;
                } else {
                    idleCounter = 0;
                }
                sendMessage();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendMessage() throws IOException, InterruptedException {

        Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();

        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            if (key.isConnectable()) {
                // socket connected
                SocketChannel sc = (SocketChannel) key.channel();
                if (sc.isConnectionPending()) {
                    sc.finishConnect();
                }
                // send first message
                this.sendFirstMsg();
            }
            if (key.isReadable()) {
                // msg received
                SocketChannel sc = (SocketChannel) key.channel();
                int count = sc.read(temp);
                if (count < 0) {
                    sc.close();
                    continue;
                }
                // 切换buffer到读状态, 内部指针归位
                temp.flip();
                String msg = CHARSET.decode(temp).toString();
                LOGGER.info("Client received [{}] from server address: {}", msg, sc.getRemoteAddress());

//                Thread.sleep(5);
                // echo back
                sc.write(ByteBuffer.wrap(msg.getBytes(CHARSET)));

                // 清空buffer
                temp.clear();
            }
        }
    }
}
