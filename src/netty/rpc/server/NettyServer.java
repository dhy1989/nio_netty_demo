package netty.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author dinghy
 * @date 2020/7/31 11:28
 */
public class NettyServer {
    public static void startServer(String hostname,int port){
        EventLoopGroup bossGroup=new NioEventLoopGroup(1);
        EventLoopGroup workGroup=new NioEventLoopGroup();
        ServerBootstrap serverBootstrap=new ServerBootstrap();
        try {
            serverBootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new BusinessHandler());
                        }
                    });
            ChannelFuture sync = serverBootstrap.bind(hostname, port).sync();
            sync.channel().closeFuture();
        } catch (InterruptedException e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        startServer("127.0.0.1",8888);
    }
}
