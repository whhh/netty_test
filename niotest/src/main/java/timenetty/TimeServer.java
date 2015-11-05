package timenetty;

/**
 * Created by Administrator on 2015/11/5 0005.
 */
public class TimeServer {

    public static void main(String[] args) {
        int port = 8080;
        if(args != null && args.length > 0){
            try {
                port = Integer.parseInt(args[0]);
            }catch (Exception e){
                //采用默认值
            }
            MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
            new Thread(timeServer,"NIO-MultiplexerTimeServer-001").start();
        }
    }
}
