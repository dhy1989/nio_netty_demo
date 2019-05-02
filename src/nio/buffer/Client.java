package nio.buffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @author dinghy
 * @date 2019/4/27
 */
public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        //1.获取到通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
        //2.切换到非阻塞模式
        socketChannel.configureBlocking(false);
        //3.分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //4.发送数据到服务端
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String next = scanner.next();
            buffer.put(next.getBytes());
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
            System.out.println(next);
            int len = 0;
            while ((len = socketChannel.read(buffer)) ==0) {
                Thread.sleep(2000);
                System.out.println("waiting from server");
            }
            buffer.flip();
            System.out.println(new String(buffer.array(), 0, len));

            // 清空缓存。
            buffer.clear();
        }
        //5.关闭通道
        socketChannel.close();
    }
}
