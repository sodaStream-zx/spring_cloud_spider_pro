/*
package spider.myspider.redisSpider;


import commoncore.entity.requestEntity.CrawlDatum;
import commoncore.parseTools.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import spider.spiderCore.crawldb.AbstractGenerator;
import spider.spiderCore.crawldb.IGeneratorFilter;
import spider.spiderCore.spiderConfig.DefaultConfigImp;

import javax.annotation.Resource;

*/
/**
 * @author 一杯咖啡
 *//*

@Component
public class RedisGenerator extends AbstractGenerator<String> {
    private static Logger LOG = LoggerFactory.getLogger(DefaultConfigImp.class);

    @Autowired
    private SerializeUtil serializeUtil;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    public RedisGenerator(IGeneratorFilter filter) {
        this.setTopN(topNumber);
        this.setFilter(filter);
    }

    @Value(value = "${generator.topNumber}")
    private int topNumber;

    */
/**
 * desc: 数据库提取任务实现
 **//*

    @Override
    public CrawlDatum nextWithoutFilter() {
        String datumString;
        CrawlDatum datum = null;
        String parse = getIDataBase().getFetchDB();
        datumString = redisTemplate.opsForList().leftPop(parse);
        LOG.debug("redis 待请求任务序列化字符串 : " + datumString);
        if (datumString != null) {
            try {
                datum = (CrawlDatum) serializeUtil.deserializeToObject(datumString);
            } catch (Exception e) {
                LOG.error("redis Generator 反序列化 出错"+e.getMessage());
            }
        }
        return datum;
    }

    @Override
    public void close() {}

    public SerializeUtil getSerializeUtil() {
        return serializeUtil;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }
}
*/
