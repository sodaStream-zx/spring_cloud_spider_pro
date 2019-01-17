package commoncore.customUtils;

import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * @author Twilight
 * @desc 休眠工具 静态类，不存在线程安全，其他实例访问时会自动复制一份。
 * @createTime 2019-01-16-12:28
 */
public class SleepUtil {
    private static final Logger log = Logger.getLogger(SleepUtil.class);

    /**
     * desc: 线程休眠
     **/
    public static void pause(int second, long mills) {
        try {
            TimeUnit.SECONDS.sleep(second);
            TimeUnit.MILLISECONDS.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("休眠出错-->> " + e.getCause() + " :: " + e.getMessage());
        }
    }
}
