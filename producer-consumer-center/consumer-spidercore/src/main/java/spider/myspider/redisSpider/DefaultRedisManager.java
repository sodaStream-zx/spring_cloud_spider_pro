package spider.myspider.redisSpider;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import spider.spiderCore.crawldb.IDataBase;
import spider.spiderCore.crawldb.IDbManager;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2019-01-04-13:11
 */
@Component
public class DefaultRedisManager implements IDbManager {
    private static final Logger log = Logger.getLogger(DefaultRedisDataBase.class);
    private IDataBase iDataBase;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired(required = false)
    public DefaultRedisManager(IDataBase iDataBase) {
        this.iDataBase = iDataBase;
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

    @Override
    public void merge() {
        String seeds = (String) iDataBase.getCrawlDB();
        String fetchs = (String) iDataBase.getFetchDB();
        while (redisTemplate.opsForList().size(seeds) > 0) {
            String seedStr = (String) redisTemplate.opsForList().leftPop(seeds);
            redisTemplate.opsForList().rightPush(fetchs, seedStr);
        }
        log.info("合并任务库");
    }
}
