package threadtest;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/12/22.
 */
public class SleepUtils {
    public static final void second(long seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        }catch (Exception e){

        }
    }
}
