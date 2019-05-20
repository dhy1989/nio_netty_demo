package netty.demo2;


import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author dinghy
 * @date
 */
public class BusiHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("通道被激活");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        System.out.println("读取消息");
        String result="";
        //接收到完成的http请求
        FullHttpRequest request = (FullHttpRequest) msg;
        String body = request.content().toString(CharsetUtil.UTF_8);
        String path = request.uri();
        HttpMethod method = request.method();
        if (!"/test".equalsIgnoreCase(path)) {
            result = "非法请求："+path;
            send(result,ctx,HttpResponseStatus.BAD_REQUEST);
            return;
        }

        //处理http GET请求
        if(HttpMethod.GET.equals(method)){
            System.out.println("body:"+body);
            result="Get request,Response="+RespConstant.getNews();
            send(result,ctx,HttpResponseStatus.OK);
            return;
        }
        //处理http POST请求
        if(HttpMethod.POST.equals(method)){
            //.....

        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        System.out.println("接收数据成功");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    private void send(String content, ChannelHandlerContext ctx,
                      HttpResponseStatus status) {
        FullHttpResponse response =
                new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                        Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,
                "text/plain;charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

    }
}
