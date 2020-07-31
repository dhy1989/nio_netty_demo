package netty.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import netty.rpc.service.HelloService;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author dinghy
 * @date 2020/7/31 12:46
 */
public class NettyClient {
    public static ExecutorService executorService= Executors.newFixedThreadPool(5);
    public static NettyClientHandler client;

    public Object getBean(final Class<?> serviceClass,final String providerName) {
          return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class<?>[]{serviceClass},(proxy,method,args)->{
              if(client==null){
                  initClient();
              }
              client.setPara(providerName+args[0]);

              return executorService.submit(client).get();
          });
    }
    public static void initClient(){
        client =new NettyClientHandler();
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(client);
                    }
                });
        try {
             bootstrap.connect("127.0.0.1", 8888).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NettyClient nettyClient=new NettyClient();
        String protocol="HelloService#";
        HelloService bean = (HelloService) nettyClient.getBean(HelloService.class, protocol);
        String hello = bean.hello("你好呀!!");
        System.out.println(hello);
    }
}
