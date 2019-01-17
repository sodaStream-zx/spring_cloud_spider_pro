package spider.myspider.redisComponent;

import commoncore.customUtils.SerializeUtil;
import commoncore.entity.requestEntity.CrawlDatum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import spider.spiderCore.idbcore.IDataBase;
import spider.spiderCore.idbcore.IGenerator;
import spider.spiderCore.idbcore.IGeneratorFilter;

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
    private IGeneratorFilter<CrawlDatum> filter = null;
    @Value(value = "${generator.topNumber}")
    private int topN;
    @Autowired
    private RedisTemplate redisTemplate;
    //提取工具计数器
    private int totalGenerate = 0;

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
                if (filter != null) {
                    if (filter.filter(datum) == null) {
                        continue;
                    }
                }
                totalGenerate += 1;
                return datum;
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
        //LOG.info("redis 提取任务" + (datumString == null ? "no propeties" : "ok"));
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
    public boolean clear() {
        this.totalGenerate = 0;
        return true;
    }

    @Override
    public boolean close() {
        return true;
    }

    @Override
    public String toString() {
        return "DefaultRedisGenerator{" +
                "IDataBase=" + IDataBase +
                ", filter=" + filter +
                ", topN=" + topN +
                ", totalGenerate=" + totalGenerate +
                ", redisTemplate=" + redisTemplate +
                '}';
    }
}
