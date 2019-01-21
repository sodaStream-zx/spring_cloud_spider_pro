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

import java.util.Optional;

/**
 * @author 一杯咖啡
 * @desc redis数据库提取提取工具
 * @createTime 2019-01-04-13:30
 */
@Component
public class DefaultRedisGenerator implements IGenerator<CrawlDatum> {
    public static final Logger LOG = LoggerFactory.getLogger(DefaultRedisGenerator.class);
    private IDataBase<String> IDataBase;
    private IGeneratorFilter<CrawlDatum> filter;
    private int topN;
    private RedisTemplate redisTemplate;
    //提取工具计数
    private int totalGenerate = 0;

    @Autowired
    public DefaultRedisGenerator(@Autowired(required = false) IGeneratorFilter<CrawlDatum> filter,
                                 IDataBase<String> IDataBase,
                                 RedisTemplate redisTemplate,
                                 @Value(value = "${generator.topNumber}") int topN) {
        this.filter = filter;
        this.IDataBase = IDataBase;
        this.redisTemplate = redisTemplate;
        this.topN = topN;
    }

    @Override
    public CrawlDatum next() {
        if (topN > 0 && totalGenerate >= topN) {
            return null;
        }
        CrawlDatum datum;
        while (true) {
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
        }
    }

    @Override
    public CrawlDatum nextWithoutFilter() {
        String datumString;
        String parse = IDataBase.getFetchDB();
        datumString = (String) redisTemplate.opsForList().leftPop(parse);
        //LOG.info("redis 提取任务" + (datumString == null ? "no propeties" : "ok"));
        if (datumString != null) {
            Optional<CrawlDatum> datum = SerializeUtil.deserializeToObject(datumString);
            if (datum.isPresent()) {
                return datum.get();
            }
        }
        return null;
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
