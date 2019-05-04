package nio.buffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author dinghy
 * @date 2019/4/27
 */
public class Server {
    public static void main(String[] args) throws IOException{
        //1.获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //2.切换到非阻塞模式
        serverSocketChannel.configureBlocking(false);
        //3.绑定端口
        serverSocketChannel.bind(new InetSocketAddress(8888));
        //4.获取选择器
        Selector selector = Selector.open();
        //5.将通道注册到选择器上,指定监听事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //6.轮询式的获取选择器上的绑定事件
        while (selector.select() > 0) {
            //7.获取选择器上所有注册的选择键
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey sk = it.next();
                //8.判断具体什么事件准备就绪
                if (sk.isAcceptable()) {
                    //10.如果接受就绪,获取客户端连接
                    SocketChannel accept = serverSocketChannel.accept();
                    //11.切换到非阻塞模式
                    accept.configureBlocking(false);
                    //12.将该通道注册到选择器上
                    accept.register(selector, SelectionKey.OP_READ);
                } else if (sk.isReadable()) {
                    //13.获取当前选择器上读就绪的通道
                    SocketChannel channel = (SocketChannel) sk.channel();
                    //14.读取数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len = 0;
                    while ((len = channel.read(buffer)) >0) {
                        buffer.flip();
                        System.out.println(new String(buffer.array(), 0, len));
                        buffer.clear();
                    }
                    channel.register(selector,SelectionKey.OP_WRITE);
                }else if(sk.isWritable()){
                    SocketChannel channel= (SocketChannel) sk.channel();
                    Scanner scanner = new Scanner(System.in);
                    String line = scanner.nextLine();
                    ByteBuffer buffer  =ByteBuffer.allocate(1024);
                    buffer.put(line.getBytes());
                    buffer.flip();
                    channel.write(buffer);
                    buffer.clear();
                    channel.register(selector, SelectionKey.OP_READ);
                }
                //15.取消选择键
                it.remove();

            }


        }
    }


}


