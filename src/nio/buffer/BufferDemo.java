package nio.buffer;

import java.nio.ByteBuffer;

/**
 * 缓冲区
 * int mark---通过mark()方法指定Buffer中的一个特定的position,之后可以调用reset()方法恢复到这个position
 * int position---下一个要读取,或者写入的索引
 * int limit---limit后的数据都不可读,并且不能大于capacity
 * int capacity---Buffer的最大容量
 * 0<=position<=limit<=capacity
 *
 * @author dinghy
 * @date 2019/4/21
 */
public class BufferDemo {
    public static void main(String[] args) {
        //1.创建一个Buffer
        String str = "abcde";
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //2.向buffer放入数据
        buffer.put(str.getBytes());
        //3.读取数据
        //读取之前需要先调用flip(),转换为读取模式
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());
        System.out.println("-------------------------------");

        buffer.flip();
        byte[] dst = new byte[buffer.limit()];
        buffer.get(dst, 0, 2);
        buffer.mark();
        System.out.println(new String(dst, 0, 1));
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());
        buffer.get(dst, 2, 2);
        System.out.println("-------------------------------");
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());
        //恢复到mark
        buffer.reset();
        System.out.println("-------------reset---------");
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());
    }
}
