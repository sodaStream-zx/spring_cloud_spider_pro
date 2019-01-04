package spider.myspider.redisSpider;

import commoncore.entity.requestEntity.CrawlDatum;
import org.springframework.stereotype.Component;
import spider.spiderCore.crawldb.IGeneratorFilter;

/**
 * @author 一杯咖啡
 */
@Component
public class StatusIGeneratorFilter implements IGeneratorFilter {
    /**
     * desc: 判断url携带的元数据 中的状态 过滤已爬取的
     * @Return: CrawlDatumn
     **/
    @Override
    public CrawlDatum filter(CrawlDatum datum) {
        if(datum.getStatus() == CrawlDatum.STATUS_DB_SUCCESS){
            return null;
        }else{
            return datum;
        }
    }
}
