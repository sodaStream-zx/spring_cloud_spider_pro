package spider.spiderCore.crawler;

import commoncore.entity.requestEntity.CrawlDatum;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-07-15:25
 */
public interface IExecutor<T> {
    T execute(CrawlDatum datum);
}
