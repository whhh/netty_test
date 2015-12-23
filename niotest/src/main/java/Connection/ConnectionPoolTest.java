package Connection;

import javax.sound.midi.Soundbank;
import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2015/12/23.
 */
@SuppressWarnings("all")
public class ConnectionPoolTest {
    static ConnectionPool pool = new ConnectionPool(10);
    //��֤����ConnectionRunner�ܹ�ͬʱ��ʼ
    static CountDownLatch start = new CountDownLatch(1);
    //main�߳̽���ȴ�����ConnectionRunner��������ܼ���ִ��
    static CountDownLatch end;

    public static void main(String[] args) throws  Exception{
        //�߳������������޸��߳��������й۲�
        int threadCount = 10;
        end = new CountDownLatch(threadCount);
        int count = 20;
        AtomicInteger got = new AtomicInteger();
        AtomicInteger notGot = new AtomicInteger();
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new ConnetionRunner(count,got,notGot),"ConnectionRunnaerThread");
            thread.start();
        }
        start.countDown(); //״̬��1�����ռ���0
        end.await();
        System.out.println("total invoke: " + (threadCount * count));
        System.out.println("got connection :" + got);
        System.out.println("not got connection " + notGot);


    }
    static class ConnetionRunner implements Runnable{
        int count ;
        AtomicInteger got ;
        AtomicInteger notGot;
        public ConnetionRunner(int count , AtomicInteger got ,AtomicInteger notGot){
            this.count = count;
            this.got = got;
            this.notGot = notGot;
        }
        @Override
        public void run() {
            try {
                start.await();//�ȴ�begin��״̬Ϊ0
            }catch (Exception e ){}
            while(count > 0){
                try {
                    //���̳߳��л�ȡ���ӣ����1000ms���޷���ȡ�������᷵��null
                    //�ֱ�ͳ�����ӻ�ȡ������got��δ��ȡ��������notGot
                    Connection connection = pool.fetchConnection(1000);
                    if(connection != null){
                        try {
                            connection.createStatement();
                            connection.commit();
                        }finally {
                            pool.releaseConnection(connection);
                            got.incrementAndGet();
                        }
                    }else{
                        notGot.incrementAndGet();
                    }
                }catch (Exception e){

                }finally {
                    count --;
                }
            }
            end.countDown();
        }
    }
}
