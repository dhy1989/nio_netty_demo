package nio.buffer;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 通道(channel)
 * 用于源节点和目标节点的连接,在Java Nio中负责缓冲区中数据的传输,Channel本身并不参与数据存储,隐刺需要缓冲区配合
 * 通道的主要实现类
 * java.nio.channels.Channel接口:
 * |--FileChannel
 * |--SocketChannel
 * |--ServerSocketChannel
 * |--DatagramChannel
 * 获取通道方式:
 * 1.getChannel()
 * 本地IO:FileInputStream,FileOutputStream,RandomAccessFile
 * 网络IO:Socket,ServerSocket,DatagramSocket
 * 2.针对各个通道提供了静态方法open()
 * 3.Files工具类的newByteChannel()
 * 通道之间的数据传输
 * transferFrom()
 * transferTo()
 * 分散和聚集
 * 分散读取（Scattering Reads）：将通道中的数据分散到多个缓冲区中
 * 聚集写入（Gathering Writes）：将多个缓冲区中的数据聚集到通道中
 * 字符集：Charset
 * 编码：字符串 -> 字节数组
 * 解码：字节数组  -> 字符串
 *
 * @author dinghy
 * @date 2019/4/21
 */
public class ChannelDemo {


    /**
     * 利用通道完成文件的复制（非直接缓冲区）
     */
    @Test
    public void test1() throws IOException {
        long start = System.currentTimeMillis();
        FileInputStream fis = new FileInputStream("D:\\movie\\1.mp4");
        FileOutputStream fos = new FileOutputStream("D:\\movie\\2.mp4");
        //获取通道
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();
        //创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (inChannel.read(buffer) != -1) {
            buffer.flip();
            //写入通道
            outChannel.write(buffer);
            //清空缓存区,如果不清空会出现问题的
            buffer.clear();
        }
        if (outChannel != null) {
            outChannel.close();
        }
        if (inChannel != null) {
            inChannel.close();
        }
        if (fos != null) {
            fos.close();
        }
        if (fis != null) {
            fis.close();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    /**
     * 使用直接缓冲区完成文件的复制(内存映射文件)
     */
    @Test
    public void test2() throws IOException {
        long start = System.currentTimeMillis();
        FileChannel inChannel = FileChannel.open(Paths.get("D:\\movie\\1.mp4"), StandardOpenOption.READ);
        FileChannel outChanel = FileChannel.open(Paths.get("D:\\movie\\2.mp4"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
        //内存映射文件
        MappedByteBuffer inMapperdBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMapperdBuffer = outChanel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());
        //直接对缓冲区进行数据的读写操作
        byte[] dst = new byte[inMapperdBuffer.limit()];
        inMapperdBuffer.get(dst);
        outMapperdBuffer.put(dst);
        inChannel.close();
        outChanel.close();
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    /**
     * 通道之间的数据传输(直接缓冲区)
     */
    @Test
    public void test3() throws IOException {
        long l = System.currentTimeMillis();
        FileChannel inChannel = FileChannel.open(Paths.get("D:/movie/h.mkv"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("D:/movie/c.mkv"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
      //使用transferTo直接传输会出现数据丢失,原因是系统允许最大传输长度,可以分多次传
       // inChannel.transferTo(0, inChannel.size(), outChannel); //17809
        /*int buff=1024*1024;
        long count= inChannel.size()/buff;
        long start=0;
        for (int i = 0; i <count ; i++) {
            inChannel.transferTo(start,buff,outChannel);
            start +=buff;
        }
        long end=inChannel.size()%buff;
        inChannel.transferTo(start,end,outChannel);*/
        outChannel.transferFrom(inChannel,0,inChannel.size());
        inChannel.close();
        outChannel.close();
       long j= System.currentTimeMillis();
        System.out.println(j-l);
    }
}
