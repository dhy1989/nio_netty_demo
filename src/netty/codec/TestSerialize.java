package netty.codec;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


/**
 * @author dinghy
 * @date 2020/7/15 13:38
 */
public class TestSerialize {
    @Test
    public void testJavaSeriable() throws IOException {
        Student student = new Student();
        student.setId(1);
        student.setName("吕布");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream1 = new ObjectOutputStream(outputStream);
        outputStream1.writeObject(student);
        System.out.println("Serializable=====" + outputStream.toByteArray().length);
    }

    @Test
    public void testProtobuf() throws IOException {
        StudentPojo.Student student = StudentPojo.Student.newBuilder().setId(1).setName("吕布").build();
        System.out.println("Serializable=====" + student.toByteArray().length);
        StudentPojo.Student student1 = StudentPojo.Student.parseFrom(student.toByteArray());
        System.out.println(student1.getId()+"-"+student1.getName());
    }

}
