package Connection;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * Created by Administrator on 2015/12/23.
 */
@SuppressWarnings("all")
public class ConnectionPool {
    private LinkedList<Connection> pool = new LinkedList<Connection>();

    //��ʼ������
    public ConnectionPool(int initialSize){
        if(initialSize >0){
            for (int i = 0; i <initialSize ; i++) {
                pool.addLast(ConnectionDriver.createConnection());
            }
        }
    }
    //�������� ����֪ͨ�����߳�
    public void releaseConnection(Connection connection){
        if(connection != null){
            synchronized (pool){
                //���Ӻ���Ҫ����֪ͨ�����������������ܹ���֪���ӳ����Ѿ��黹��һ������
                pool.addLast(connection);
                pool.notifyAll();
            }
        }
    }
    //��mills���޷���ȡ�����ӣ����᷵��null
    public Connection fetchConnection(long mills)throws Exception{
        synchronized (pool){
            //��ȫ��ʱ
            if(mills <= 0){
                //���û��Ԫ��
                while(pool.isEmpty()){
                    pool.wait();
                }
                return pool.removeFirst();
            }else {
                long future = System.currentTimeMillis() + mills ;
                long remaining = mills;
                while (pool.isEmpty() && remaining > 0){
                    pool.wait(remaining);
                    remaining = future - System.currentTimeMillis();
                }
                Connection result = null;
                if(!pool.isEmpty()){
                    result = pool.removeFirst();
                }
                return result;
            }
        }

    }
}