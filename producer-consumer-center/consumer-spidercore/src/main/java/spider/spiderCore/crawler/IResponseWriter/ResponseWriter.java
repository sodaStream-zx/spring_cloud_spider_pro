package spider.spiderCore.crawler.IResponseWriter;

import commoncore.entity.httpEntity.ResponsePage;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-11-30-13:12
 */
public interface ResponseWriter {
    boolean writerResponsePageToRedis(ResponsePage responsePage);
}
