package threadtest;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/12/23.
 */
@SuppressWarnings("all")
public class Join {

    public static void main(String[] args) throws  Exception{
        Thread previous = Thread.currentThread();
        for (int i = 0; i <10 ; i++) {
            //ÿ���߳�ӵ��ǰһ�����ã���Ҫ�ȴ�һ���߳���ֹ�����ܹ�����ȥ
            Thread thread = new Thread(new Domino(previous),String.valueOf(i));
            thread.start();
            previous = thread;
        }
        TimeUnit.SECONDS.sleep(5);
        System.out.println(Thread.currentThread().getName() + " terminate.");

    }

    static class Domino implements Runnable{
        private Thread thread;
        public Domino(Thread thread){
            this.thread = thread;
        }
        @Override
        public void run() {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ",terminate");
        }
    }
}


