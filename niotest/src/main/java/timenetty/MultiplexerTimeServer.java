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
    //��·�ɸ�����
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private volatile boolean stop;

    /**
     * ��ʼ����·�ɸ��������󶨼����˿�
     * @param port
     */
    public MultiplexerTimeServer(int port){
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            //����Ϊ������ģʽ
            serverChannel.configureBlocking(false);
            //���󶨶˿ڣ������жӵ���󳤶ȣ�
            serverChannel.socket().bind(new InetSocketAddress(port), 1024);
            //ע���·�ɸ�����
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
                //����˯��1s
                selector.select(1000);
                //���� ͨ����
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
                            //�Ѹĵ�ǰͨ�� �Ƴ���
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
        //��·�������رպ�����ע���������Channel��Pipe�ȴ���Դ����
        //�Զ�ȥע�Ტ�ҹرգ����Բ���Ҫ�ظ��ͷ���Դ
        if(selector != null){
            try {
                selector.close();
            }catch (Exception e){
            e.printStackTrace();
            }
        }

    }
    //����һ��ͨ��
    private void handleInput(SelectionKey key ) throws Exception {
        //�жϸ�ͨ���Ƿ���Ч
        if(key.isValid()){
        //�����½����������Ϣ
            if(key.isAcceptable()){
            //���ظ���Կ��ͨ��
                ServerSocketChannel ssc =(ServerSocketChannel)key.channel();
                //�������Ӹ�ͨ��
                SocketChannel ss =ssc.accept();
                //���÷�����ͨѶ
                ss.configureBlocking(false);
                // ע���ͨ��
                ss.register(selector,SelectionKey.OP_READ);
            }
            //���������Կ��ͨ���Ƿ�׼���ö�ȡ��
            if (key.isReadable()){
                //���ظ�ͨ��
                SocketChannel sc = (SocketChannel)key.channel();
                //׼��������
                ByteBuffer  readBuffer = ByteBuffer.allocate(1024);
                //�ܵ���ȡ���� �ŵ�������
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
                    //�Զ���·�ر�
                    key.cancel();
                    sc.close();
                }else{
                    ;//��ȡ��0 �ֽں���
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
