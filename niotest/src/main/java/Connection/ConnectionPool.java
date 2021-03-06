package Connection;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * Created by Administrator on 2015/12/23.
 */
@SuppressWarnings("all")
public class ConnectionPool {
    private LinkedList<Connection> pool = new LinkedList<Connection>();

    //初始化连接
    public ConnectionPool(int initialSize){
        if(initialSize >0){
            for (int i = 0; i <initialSize ; i++) {
                pool.addLast(ConnectionDriver.createConnection());
            }
        }
    }
    //添加连接 并并通知其他线程
    public void releaseConnection(Connection connection){
        if(connection != null){
            synchronized (pool){
                //连接后需要进行通知，这样其他消费者能够感知连接池中已经归还了一个连接
                pool.addLast(connection);
                pool.notifyAll();
            }
        }
    }
    //在mills内无法获取到连接，将会返回null
    public Connection fetchConnection(long mills)throws Exception{
        synchronized (pool){
            //完全超时
            if(mills <= 0){
                //如果没有元素
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
