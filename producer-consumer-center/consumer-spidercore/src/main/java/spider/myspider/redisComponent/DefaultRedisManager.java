package spider.myspider.redisComponent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import spider.spiderCore.idbcore.IDataBase;
import spider.spiderCore.idbcore.IDbManager;

/**
 * @author 一杯咖啡
 * @desc redis 数据库管理操作
 * @createTime 2019-01-04-13:11
 */
@Component
public class DefaultRedisManager implements IDbManager {
    private static final Logger log = Logger.getLogger(DefaultDataUtil.class);
    private IDataBase iDataBase;
    private RedisTemplate redisTemplate;

    @Autowired
    public DefaultRedisManager(IDataBase iDataBase,
                               RedisTemplate redisTemplate) {
        this.iDataBase = iDataBase;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isDBExists() {
        return true;
    }

    @Override
    public void clear() {

    }

    @Override
    public void open() {

    }

    @Override
    public void close() {
    }

    /**
     * desc:将入口url和待抓取任务url合并
     **/
    @Override
    public void merge() {
        String seeds = (String) iDataBase.getCrawlDB();
        String fetch = (String) iDataBase.getFetchDB();
        while (redisTemplate.opsForList().size(seeds) > 0) {
            String seedStr = (String) redisTemplate.opsForList().leftPop(seeds);
            redisTemplate.opsForList().rightPush(fetch, seedStr);
        }
        log.info("合并任务库");
    }
}
