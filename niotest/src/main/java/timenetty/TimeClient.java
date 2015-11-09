package timenetty;

import nio.*;
import timenetty.TimeClientHandle;

/**
 * Created by Administrator on 2015/11/9 0009.
 */
public class TimeClient {

    public static void main(String[] args) throws  Exception{
        int port =8080;
        if(args != null && args.length > 0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (Exception e){
                //����Ĭ�ϵ�
            }
        }
        new Thread(new TimeClientHandle("127.0.0.1",port),"TimeClient-001").start();
    }
}
