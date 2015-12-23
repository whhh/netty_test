package threadtest;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/12/22.
 */
@SuppressWarnings("all")
public class Interrupted {

    public static void main(String[] args)throws Exception {
        //sleepThread 不停的尝试睡眠
        Thread sleepThread = new Thread(new SleepRunner(), "SleepThread");
        sleepThread.setDaemon(true);
        //busyThread不停止运行
        Thread busyThread = new Thread(new BusyRunner(),"Busythread");
        busyThread.setDaemon(true);
        busyThread.start();
        sleepThread.start();
        //休眠5秒，让sleepThread和busyThread充分运行
        TimeUnit.SECONDS.sleep(5);
        sleepThread.interrupt();
        busyThread.interrupt();
        System.out.println("SleepThread interrupted " + sleepThread.isInterrupted());
        System.out.println("BusyThread interrupted " + busyThread.isInterrupted());
        SleepUtils.second(2);


    }

    static class SleepRunner implements Runnable {

        @Override
        public void run() {
            while(true){
                SleepUtils.second(10);
            }
        }
    }
    static class BusyRunner implements Runnable{

        @Override
        public void run() {
            while(true){

            }
        }
    }
}


