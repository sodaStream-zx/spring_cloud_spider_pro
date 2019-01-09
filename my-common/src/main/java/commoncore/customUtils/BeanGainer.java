package commoncore.customUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-09-21:57
 */
@Component
public class BeanGainer implements ApplicationContextAware {
    private static final Logger log = Logger.getLogger(BeanGainer.class);
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * desc: 从容器中获取bean
     *
     * @Return:Bean
     **/
    public static <T> T getBean(String name, Class<T> aClass) {
        try {
            return context.getBean(name, aClass);
        } catch (Exception e) {
            log.error("未取得bean:名：" + name + "，类名 ：" + aClass.getSimpleName());
            return null;
        }
    }
}
