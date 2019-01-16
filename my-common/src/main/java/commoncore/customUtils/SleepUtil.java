package commoncore.customUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-16-12:28
 */
public class SleepUtil {
    public static void pause(int second, long mills) {
        try {
            TimeUnit.SECONDS.sleep(second);
            TimeUnit.MILLISECONDS.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
