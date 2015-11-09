package timenetty;

import java.io.IOException;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Administrator on 2015/11/5 0005.
 */
@SuppressWarnings("all")
public class MultiplexerTimeServer  implements  Runnable{
    //多路由复用器
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private volatile boolean stop;

    /**
     * 初始化多路由复用器、绑定监听端口
     * @param port
     */
    public MultiplexerTimeServer(int port){
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            //设置为非阻塞模式
            serverChannel.configureBlocking(false);
            //（绑定端口，请求列队的最大长度）
            serverChannel.socket().bind(new InetSocketAddress(port), 1024);
            //注册多路由复用器
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The time server is start in port : " + port);
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }finally {

        }

    }
    public void stop(){
        this.stop = true;
    }

    @Override
    public void run() {
        while(!stop){
            try {
                //设置睡眠1s
                selector.select(1000);
                //返回 通道集
                Set<SelectionKey> selectionKeys  = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while(it.hasNext()){
                    key = it.next();
                    it.remove();
                    try {
                       handleInput(key);
                    }catch (Exception e){
                        if(key!= null){
                            //把改当前通道 移除掉
                           key.cancel();
                            if(key.channel()!= null){
                                key.channel().close();
                            }
                        }
                    }finally {

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {

            }
        }
        //多路复用器关闭后，所有注册在上面的Channel和Pipe等待资源都会
        //自动去注册并且关闭，所以不需要重复释放资源
        if(selector != null){
            try {
                selector.close();
            }catch (Exception e){
            e.printStackTrace();
            }
        }

    }
    //接入一个通道
    private void handleInput(SelectionKey key ) throws Exception {
        //判断该通道是否有效
        if(key.isValid()){
        //处理新接入的请求信息
            if(key.isAcceptable()){
            //返回该密钥的通道
                ServerSocketChannel ssc =(ServerSocketChannel)key.channel();
                //接收连接该通道
                SocketChannel ss =ssc.accept();
                //设置非阻塞通讯
                ss.configureBlocking(false);
                // 注册该通道
                ss.register(selector,SelectionKey.OP_READ);
            }
            //测试这个密钥的通道是否准备好读取。
            if (key.isReadable()){
                //返回该通道
                SocketChannel sc = (SocketChannel)key.channel();
                //准备缓冲区
                ByteBuffer  readBuffer = ByteBuffer.allocate(1024);
                //管道读取数据 放到缓冲区
                int readBytes = sc.read(readBuffer);
                if(readBytes> 0){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes,"UTF-8");
                    System.out.println("The time server receive order: " +body);
                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?
                            new java.util.Date(System.currentTimeMillis()).toString(): "BAD ORDER";
                    doWrite(sc,currentTime);

                  } else if (readBytes < 0 ){
                    //对端链路关闭
                    key.cancel();
                    sc.close();
                }else{
                    ;//读取到0 字节忽略
                }
            }
        }
    }
    private void doWrite(SocketChannel channel,String response)throws Exception{
        if (response != null && response.trim().length() > 0 ) {
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }
    }
}
