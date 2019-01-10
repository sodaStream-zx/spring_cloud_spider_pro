package spider.spiderCore.ihttp;


import commoncore.entity.httpEntity.ResponsePage;
import commoncore.entity.requestEntity.CrawlDatum;

/**
 *发送请求接口
 */
public interface Requester {
     ResponsePage getResponse(CrawlDatum crawlDatum) throws Exception;
}
