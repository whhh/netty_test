package threadtest;

/**
 * Created by Administrator on 2015/12/22.
 */
public class Synchronized {

    public static void main(String[] args) {
        //��Synchronized Class ���м���
        synchronized (Synchronized.class){
        }
        //��̬ͬ����������Synchronized Class������м���
        m();
    }

    public static synchronized void m(){
    }

}

