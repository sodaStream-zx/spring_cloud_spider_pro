package spider.spiderCore.ihttp;


import commoncore.entity.httpEntity.ResponseData;
import commoncore.entity.requestEntity.CrawlDatum;

/**
 *发送请求接口
 */
public interface Requester {
    ResponseData getResponse(CrawlDatum crawlDatum) throws Exception;
}
