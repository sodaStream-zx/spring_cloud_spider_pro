package spider.spiderCore.http;

import commoncore.entity.responseEntity.CrawlDatum;
import spider.spiderCore.http.httpImp.HttpResponse;

import java.net.HttpURLConnection;

/**
 * @author 一杯咖啡
 * @desc 发送请求，包装请求
 * @createTime 2018-12-28-0:22
 */
public interface ISendRequest<T> {
    /**
     * desc: 配置请求
     **/
    void configHttpRequest(HttpURLConnection httpURLConnection);

    /**
     * desc: 获取返回值
     **/
    HttpResponse sendRequest(CrawlDatum crawlDatum) throws Exception;

    /**
     * desc:转换 封装 响应数据
     **/
    T converterResponsePage(CrawlDatum crawlDatum);
}
