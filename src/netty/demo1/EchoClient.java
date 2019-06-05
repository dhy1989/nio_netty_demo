package netty.demo1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author dinghy
 * @date
 */
public class EchoClient {
    private int port;
    private String host;

    public EchoClient(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public void start() throws InterruptedException {
        //线程组
        EventLoopGroup group = new NioEventLoopGroup();
        //客户端启动必备
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(group)
                    //指明NIO进行网络通讯
                    .channel(NioSocketChannel.class)
                    //配置远程服务地址
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new EchoClientHandler());
            //连接到远程节点,阻塞等待直到连接完成
            ChannelFuture f = bootstrap.connect().sync();
            //阻塞,直到channel关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new EchoClient(9000, "127.0.0.1").start();
    }
}
