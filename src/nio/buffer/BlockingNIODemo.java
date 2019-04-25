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
public class BlockingNIODemo {
    /**
     * 客户端
     */
    @Test
    public void test1() throws IOException {
        //1.获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 10000));
        FileChannel fileChannel = FileChannel.open(Paths.get("doc/1.jpg"), StandardOpenOption.READ);
        //2.分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //3.读取本地文件,传到服务器
        while (fileChannel.read(buffer) != -1) {
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }
        socketChannel.close();
        fileChannel.close();
    }

    /**
     * 服务端
     */
    @Test
    public void test2() throws IOException {
        //1.获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        FileChannel fileChannel = FileChannel.open(Paths.get("doc/2.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        //2.绑定连接
        serverSocketChannel.bind(new InetSocketAddress(10000));
        //3.获取客户端的通道连接
        SocketChannel acceptChannel = serverSocketChannel.accept();
        //4.分配指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //5.接收客户端的数据
        while (acceptChannel.read(byteBuffer)!=-1){
            byteBuffer.flip();
            fileChannel.write(byteBuffer);
            byteBuffer.clear();
        }
        //6.关闭通道
        acceptChannel.close();
        fileChannel.close();
        serverSocketChannel.close();
    }
}
