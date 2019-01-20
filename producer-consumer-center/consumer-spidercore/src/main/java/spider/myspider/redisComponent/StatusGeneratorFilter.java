package spider.myspider.redisComponent;

import commoncore.entity.requestEntity.CrawlDatum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spider.spiderCore.idbcore.IGeneratorFilter;

/**
 * @author 一杯咖啡
 * @desc 爬虫任务状态 最大执行次数过滤
 */
@Component
public class StatusGeneratorFilter implements IGeneratorFilter<CrawlDatum> {
    @Value(value = "${igenerator.maxExecuteCount}")
    private int maxExecuteCount;

    /**
     * desc: 判断url携带的元数据 中的状态 过滤已爬取的
     *
     * @Return: CrawlDatumn
     **/
    @Override
    public CrawlDatum filter(CrawlDatum datum) {
        if (datum.getStatus() == CrawlDatum.STATUS_DB_SUCCESS && datum.getExecuteCount() > maxExecuteCount) {
            return null;
        } else {
            return datum;
        }
    }
}
