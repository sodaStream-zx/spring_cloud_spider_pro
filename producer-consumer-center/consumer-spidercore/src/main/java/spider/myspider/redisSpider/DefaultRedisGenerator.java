package spider.myspider.redisSpider;

import commoncore.customUtils.SerializeUtil;
import commoncore.entity.requestEntity.CrawlDatum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import spider.spiderCore.crawldb.IDataBase;
import spider.spiderCore.crawldb.IGenerator;
import spider.spiderCore.crawldb.IGeneratorFilter;

/**
 * @author 一杯咖啡
 * @desc redis数据库提取提取工具
 * @createTime 2019-01-04-13:30
 */
@Component
public class DefaultRedisGenerator implements IGenerator<CrawlDatum> {
    public static final Logger LOG = LoggerFactory.getLogger(DefaultRedisGenerator.class);
    @Autowired
    private IDataBase<String> IDataBase;
    @Autowired(required = false)
    private IGeneratorFilter filter = null;
    @Value(value = "${generator.topNumber}")
    private int topN = 0;
    @Value(value = "crawldatum.maxExecuteCount")
    private int maxExecuteCount;
    @Autowired
    private RedisTemplate redisTemplate;


    private int totalGenerate;

    @Override
    public CrawlDatum next() {
        if (topN > 0 && totalGenerate >= topN) {
            return null;
        }
        CrawlDatum datum;
        while (true) {
            try {
                datum = nextWithoutFilter();
                if (datum == null) {
                    return null;
                }
                if (filter == null || (datum = filter.filter(datum)) != null) {
                    if (datum.getExecuteCount() > maxExecuteCount) {
                        continue;
                    }
                    totalGenerate += 1;
                    return datum;
                }
            } catch (Exception e) {
                LOG.info("Exception when generating", e);
                return null;
            }
        }
    }

    @Override
    public CrawlDatum nextWithoutFilter() {
        String datumString;
        CrawlDatum datum = null;
        String parse = IDataBase.getFetchDB();
        datumString = (String) redisTemplate.opsForList().leftPop(parse);
        LOG.info("redis 提取任务" + (datumString == null ? "no propeties" : "ok"));
        if (datumString != null) {
            try {
                datum = (CrawlDatum) SerializeUtil.deserializeToObject(datumString);
            } catch (Exception e) {
                LOG.error("redis Generator 反序列化 出错" + e.getMessage());
            }
        }
        return datum;
    }

    @Override
    public int totalGeneretNum() {
        return this.totalGenerate;
    }

    @Override
    public void clear() {
        this.totalGenerate = 0;
    }

    @Override
    public void close() {

    }

    @Override
    public String toString() {
        return "DefaultRedisGenerator{" +
                "IDataBase=" + IDataBase +
                ", filter=" + filter +
                ", topN=" + topN +
                ", totalGenerate=" + totalGenerate +
                ", maxExecuteCount=" + maxExecuteCount +
                ", redisTemplate=" + redisTemplate +
                '}';
    }
}
