package nio.buffer;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author dinghy
 * @date 2019/4/25
 */
public class BlockingNIOReplayDemo {
    /**
     * 客户端
     */
    @Test
    public void client() throws IOException {
        //1.创建通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
        FileChannel fileChannel = FileChannel.open(Paths.get("doc/1.jpg"), StandardOpenOption.READ);
        //2.指定缓冲区
        ByteBuffer buff = ByteBuffer.allocate(1024);
        //3.读取文件
        while (fileChannel.read(buff) != -1) {
            buff.flip();
            socketChannel.write(buff);
            buff.clear();
        }
        socketChannel.shutdownOutput();
        //接收服务端反馈
         int len=0;
        while((len=socketChannel.read(buff))!=-1){
            buff.flip();
            System.out.println(new String(buff.array(),0,len));
            buff.clear();
        }

        //4.关闭通道
        fileChannel.close();
        socketChannel.close();
    }

    /**
     * 服务端
     */
    @Test
    public void server() throws IOException {
        //1.创建服务端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        FileChannel fileChannel = FileChannel.open(Paths.get("doc/2.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        //2.绑定端口
        serverSocketChannel.bind(new InetSocketAddress(8888));
        //3.获取客户端连接
        SocketChannel acceptChannel = serverSocketChannel.accept();
        //4.指定缓冲区
        ByteBuffer buff = ByteBuffer.allocate(1024);
        //5.接收数据
        while (acceptChannel.read(buff)!=-1){
            buff.flip();
            fileChannel.write(buff);
            buff.clear();
        }
        //发送反馈给客户端
        buff.put("服务端接收数据成功".getBytes());
        buff.flip();
        acceptChannel.write(buff);

        //6.关闭连接
        acceptChannel.close();
        fileChannel.close();
        serverSocketChannel.close();
    }
}
