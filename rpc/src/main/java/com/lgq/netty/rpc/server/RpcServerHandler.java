package com.lgq.netty.rpc.server;

import com.lgq.netty.rpc.InputParam;
import com.lgq.netty.rpc.OutputParam;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author lgq
 */
public class RpcServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        InputParam inputParam = (InputParam) msg;
        System.out.println("server 接受到的数据是 " + inputParam.toString());

        OutputParam outputParam = new OutputParam();
        outputParam.setStr1("第一个数是：" + String.valueOf(inputParam.getNum1()));
        outputParam.setStr2("第二个数是：" + String.valueOf(inputParam.getNum2()));
        ctx.writeAndFlush(outputParam);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
