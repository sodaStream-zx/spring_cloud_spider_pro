package spider.myspider.redisComponent;

import commoncore.customUtils.SerializeUtil;
import commoncore.entity.requestEntity.CrawlDatum;
import commoncore.entity.requestEntity.CrawlDatums;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import spider.spiderCore.idbcore.IDataBase;
import spider.spiderCore.idbcore.IDbManager;
import spider.spiderCore.idbcore.IDbWritor;

/**
 * @author 一杯咖啡
 * @desc redis数据库写入工具
 * @createTime 2019-01-04-13:04
 */
@Component
@Scope("prototype")
public class DefaultRedisDbWritor implements IDbWritor<CrawlDatum, CrawlDatums> {
    private static final Logger log = Logger.getLogger(DefaultRedisDbWritor.class);
    @Autowired
    private IDbManager iDbManager;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IDataBase iDataBase;

    /**
     * desc: 任务强制注入
     **/
    @Override
    public void inject(CrawlDatum datum, boolean force) throws Exception {
        if (!iDbManager.isDBExists()) {
            throw new Exception("redis 数据库无效");
        } else {
            String taskString = SerializeUtil.serializeToString(datum);
            log.info("任务入口注入 ： " + datum.url());
            //注入任务到入口库
            String seeds = (String) iDataBase.getCrawlDB();
            log.debug("入口数据库 :" + seeds);
            redisTemplate.opsForList().rightPush(seeds, taskString);
        }
    }

    @Override
    public void injectList(CrawlDatums datums, boolean force) throws Exception {
        for (CrawlDatum x : datums) {
            inject(x, force);
        }
    }

    @Override
    public void initSegmentWriter() {

    }

    @Override
    public void writeFetchSegment(CrawlDatum fetchDatum) {
        String taskString;
        try {
            taskString = SerializeUtil.serializeToString(fetchDatum);
            redisTemplate.opsForList().rightPush(iDataBase.getLinkDB(), taskString);
        } catch (Exception e) {
            log.error("序列化出错");
        }
    }

    @Override
    public void writeParseSegment(CrawlDatums parseDatums) {
        String nextTask;
        try {
            if (!parseDatums.isEmpty()) {
                for (CrawlDatum task : parseDatums) {
                    nextTask = SerializeUtil.serializeToString(task);
                    redisTemplate.opsForList().rightPush(iDataBase.getFetchDB(), nextTask);
                }
                log.info("写入后续任务数量：" + parseDatums.size());
            }
        } catch (Exception e) {
            log.error("序列化出错");
        }
    }

    @Override
    public void closeSegmentWriter() {

    }

    @Override
    public String toString() {
        return "DefaultRedisDbWritor{" +
                "iDbManager=" + iDbManager +
                ", redisTemplate=" + redisTemplate +
                ", iDataBase=" + iDataBase +
                '}';
    }
}
