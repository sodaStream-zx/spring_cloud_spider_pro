package spider.spiderCore.http.IRequestor;


import commoncore.entity.responseEntity.CrawlDatum;
import commoncore.entity.responseEntity.ResponsePage;

/**
 *发送请求接口
 */
public interface Requester {
     ResponsePage getResponse(CrawlDatum crawlDatum) throws Exception;
}
