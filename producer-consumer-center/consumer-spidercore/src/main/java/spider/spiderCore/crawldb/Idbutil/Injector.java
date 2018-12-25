package spider.spiderCore.crawldb.Idbutil;

import commoncore.entity.responseEntity.CrawlDatum;

/**
 *入口注入
 */
public interface Injector {
      void inject(CrawlDatum datum) throws Exception;
}
