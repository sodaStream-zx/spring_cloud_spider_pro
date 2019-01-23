package parsercore.configs;

import commoncore.customUtils.SleepUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-23-12:09
 */
@Service
public class LoadConfigService {
    private static final Logger log = Logger.getLogger(LoadConfigService.class);
    private RedisTemplate redisTemplate;
    private String redisConfig;

    @Autowired
    public LoadConfigService(RedisTemplate redisTemplate,
                             @Value(value = "${redisDb.config}") String redisConfig) {
        this.redisTemplate = redisTemplate;
        this.redisConfig = redisConfig;
    }

    /**
     * desc: 加载reids数据库 约定配置
     **/
    public String loadRedisKeyConfig() {
        log.info("-----------加载redisKey 约定配置-----------");
        String redisStr;
        //读取redis队列任务，并开始抓取；阻塞直到能取出值
        while (true) {
            redisStr = (String) redisTemplate.opsForValue().get(redisConfig);
            if ("".equals(redisStr) || null == redisStr) {
                log.error("redis 网站配置数据为空");
                SleepUtil.pause(1, 0);
            } else {
                break;
            }
        }
        return redisStr;
    }
}
