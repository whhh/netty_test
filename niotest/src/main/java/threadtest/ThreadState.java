package threadtest;

import org.springframework.cglib.core.Block;

/**
 * Created by Administrator on 2015/12/22.
 */
@SuppressWarnings("all")
public class ThreadState {
    public static void main(String[] args) {
        new Thread(new TimeWaiting(),"TimeWaitingThread").start();
        new Thread(new Waiting(),"WaitingThread").start();
        //ʹ������Blocked�̣߳�һ����ȡ�ɹ���һ��������
        new Thread(new Blocked(),"BlockedThread-1").start();
        new Thread(new Blocked(),"BlockedThread-3").start();
        new Thread(new Blocked(),"BlockedThread-2").start();
        new Thread(new Blocked(),"BlockedThread-4").start();
        new Thread(new Blocked(),"BlockedThread-5").start();


    }
    //���̲߳��Ͻ���˯��
    static class TimeWaiting implements Runnable{

        @Override
        public void run() {
            while (true){
                SleepUtils.second(100);
            }
        }
    }
    //���߳���Waiting.classʵ���ϵȴ�
    static class Waiting implements Runnable{

        @Override
        public void run() {
            while(true){
                synchronized (Waiting.class){
                    try {
                        Waiting.class.wait();
                    }catch (Exception e){

                    }

                }
            }
        }
    }
    //���߳���Blocked.classʵ���ϼ����󣬲����ͷŸ���
    static class Blocked implements Runnable{

        @Override
        public void run() {
            synchronized (Blocked.class){
                while(true){
                    SleepUtils.second(100);
                }

            }
        }
    }
}





