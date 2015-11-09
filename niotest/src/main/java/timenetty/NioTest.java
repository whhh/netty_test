package timenetty;

import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2015/11/9 0009.
 */
public class NioTest {

    public static void main(String[] args)throws  Exception{
        ByteBuffer b = ByteBuffer.allocate(11);
        b.put((byte)'H').put((byte) 'e').put((byte)'l').put((byte)'l').put((byte) 'o');
      //  b.flip();
      //  b.limit(b.position()).position(0);
        byte[] bytes = new byte[b.remaining()];
        b.get(bytes);
        String str = new String(bytes,"UTF-8");
        System.out.println("str : " + str);
    }

}
