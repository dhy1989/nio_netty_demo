package netty.codec;

import java.io.Serializable;

/**
 * @author dinghy
 * @date 2020/7/15 13:48
 */
public class Student implements Serializable{
   public static final long serialVersionUID = 1L;
    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
