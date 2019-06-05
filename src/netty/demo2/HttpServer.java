package netty.demo2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @author dinghy
 * @date
 */
public class HttpServer {
    private int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public void start() {
        EventLoopGroup parent = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(parent, work).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                //http响应编码
                ch.pipeline().addLast("encode", new HttpResponseEncoder());
                //http请求编码
                ch.pipeline().addLast("decode", new HttpRequestDecoder());
                //聚合http请求
                ch.pipeline().addLast("aggre",
                        new HttpObjectAggregator(10 * 1024 * 1024));
                //启用http压缩
                ch.pipeline().addLast("compressor", new HttpContentCompressor());
                //自己的业务处理
                ch.pipeline().addLast("busi", new BusiHandler());
            }
        });
        try {
            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("服务端启动");
            // 监听服务器关闭监听
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            parent.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new HttpServer(9000).start();
    }
}
