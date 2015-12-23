package threadtest;

/**
 * Created by Administrator on 2015/12/22.
 */
public class Synchronized {

    public static void main(String[] args) {
        //对Synchronized Class 进行加锁
        synchronized (Synchronized.class){
        }
        //静态同步方法，对Synchronized Class对象进行加锁
        m();
    }

    public static synchronized void m(){
    }

}

