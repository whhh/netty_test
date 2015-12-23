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
            //每条线程拥有前一条引用，需要等待一个线程终止，才能够返回去
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


