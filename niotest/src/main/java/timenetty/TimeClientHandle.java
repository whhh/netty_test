package timenetty;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Administrator on 2015/11/9 0009.
 */
@SuppressWarnings("all")
public class TimeClientHandle implements  Runnable {
    private String host ;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;

    public TimeClientHandle(String host ,int port){
        this.host = host == null ? "127.0.0.1" : host ;
        this.port=port;
        try {
            //���������
            selector = Selector.open();
            //��ͨ�������÷�����
            socketChannel =SocketChannel.open();
            socketChannel.configureBlocking(false);
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        try {
            //·�ɸ�����ע��
            doConnect();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }finally {

        }
        while (!stop){
            try {
                selector.select(100);
                Set<SelectionKey> selectionKeys =selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()){
                 key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    }catch (Exception e){
                        if(key != null){
                            key.cancel();
                            if(key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }
        //��·�������رպ���������������Channel��Pipe����Դ�����Զ���Ტ�رգ�
       // ���в���Ҫ�ظ��ͷ���Դ
        if(selector !=null){
            try {

                selector.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void handleInput(SelectionKey key)throws  Exception{
        if(key.isValid()){
            //�ж��Ƿ����ӳɹ�
            SocketChannel sc = (SocketChannel)key.channel();
            if(sc.finishConnect()){
                sc.register(selector,SelectionKey.OP_READ);
                doWrite(sc);
            }else{
                System.exit(1);//����ʧ�ܣ������˳�
            }
            if(key.isReadable()){
                ByteBuffer readBuffer =ByteBuffer.allocate(1024);
                int readBytes =sc.read(readBuffer);
                if(readBytes>0){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String str = new String(bytes,"UTF-8");
                    System.out.println("Now is : " + str);
                    this.stop =true;
                }else if (readBytes < 0){
                //����·�ر�
                    key.cancel();
                    sc.close();
                }else{
                    ;//����0�ֽڣ�����
                }
            }
        }
    }
    private void doConnect()throws IOException{
        //���ֱ�����ӳɹ�����ע�ᵽ��·�������ϣ�����������Ϣ����Ӧ��
        if(socketChannel.connect(new InetSocketAddress(host,port))){
            //ע��ͨ����׼����ȡ
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel);
        }else{
            socketChannel.register(selector,SelectionKey.OP_ACCEPT);
        }
    }
    private void doWrite(SocketChannel sc)throws IOException{
        byte[] req = "QUERY TIME ORDER".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        sc.write(writeBuffer);
        if(!writeBuffer.hasRemaining()){
            System.out.println("Send order 2 server succeed.");
        }

    }
}
