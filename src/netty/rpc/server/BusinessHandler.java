package netty.rpc.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import netty.rpc.service.HelloService;
import netty.rpc.service.impl.HelloServiceImpl;

/**
 * @author dinghy
 * @date 2020/7/31 11:32
 */
public class BusinessHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("msg" + msg);
        String message = (String) msg;
        String protocol="HelloService#";
        if(message.startsWith(protocol)){
            String substring = message.substring(protocol.length(), message.length());
            String hello = new HelloServiceImpl().hello(substring);
            ctx.writeAndFlush(Unpooled.copiedBuffer(hello, CharsetUtil.UTF_8));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
