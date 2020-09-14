package com.yuzhouwan.hacker.nio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function：Echo Server Handler
 *
 * @author Benedict Jin
 * @since 2018/11/11
 */
@ChannelHandler.Sharable // 注解 @Sharable 可以让它在 channels 间共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    private static byte[] HELLO_WORLD_BYTES = "Hello World".getBytes();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = ctx.alloc().buffer();
        buf.writeBytes(HELLO_WORLD_BYTES);
        ctx.write(buf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
