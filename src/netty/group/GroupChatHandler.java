package netty.group;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Iterator;

/**
 * @author dinghy
 * @date 2020/7/2 14:51
 */
public class GroupChatHandler extends SimpleChannelInboundHandler<String> {
   private static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

   /**
    * @Description 当客户端建立连接时被执行
    * @Author dinghy
    * @Date 2020/7/2 16:36
    */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.add(channel);
        String message="客户端--"+channel.remoteAddress()+"-->上线了";
        channelGroup.writeAndFlush(message);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("收到消息了:"+msg);
        String message="客户端--"+ctx.channel().remoteAddress()+"-->发送了:"+msg;
        Iterator<Channel> iterator = channelGroup.iterator();
        while (iterator.hasNext()){
            Channel next = iterator.next();
            if(next!=ctx.channel()){
                next.writeAndFlush(message);
            }
        }
    }

}
