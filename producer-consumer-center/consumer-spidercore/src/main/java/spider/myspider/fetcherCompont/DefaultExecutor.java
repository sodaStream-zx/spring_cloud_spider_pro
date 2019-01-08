package spider.myspider.fetcherCompont;

import commoncore.entity.httpEntity.ResponsePage;
import commoncore.entity.requestEntity.CrawlDatum;
import commoncore.entity.requestEntity.CrawlDatums;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import spider.spiderCore.crawldb.IDbWritor;
import spider.spiderCore.crawler.IExecutor;
import spider.spiderCore.crawler.ISimpleParse;
import spider.spiderCore.fetcher.IFetcherTools.IContentPageFilter;
import spider.spiderCore.fetcher.IFetcherTools.INextFilter;
import spider.spiderCore.http.ISendRequest;


/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-07-15:35
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultExecutor implements IExecutor<CrawlDatums> {
    private static final Logger log = Logger.getLogger(DefaultExecutor.class);
    @Autowired
    IDbWritor iDbWritor;
    @Autowired
    ISendRequest<ResponsePage> iSendRequest;
    @Autowired
    ISimpleParse iSimpleParse;
    @Autowired
    IContentPageFilter IContentPageFilter;
    @Autowired(required = false)
    INextFilter INextFilter;

    @Override
    public CrawlDatums execute(CrawlDatum datum) {
        ResponsePage responsePage = null;
        try {
            //1.调用请求工具获取响应页面
            responsePage = iSendRequest.converterResponsePage(datum);
            log.info("done: " + datum.briefInfo());
            datum.setStatus(CrawlDatum.STATUS_DB_SUCCESS);
        } catch (Exception e) {
            log.info("failed: " + datum.briefInfo());
            log.error(e.toString());
            datum.setStatus(CrawlDatum.STATUS_DB_FAILED);
        }
        // 2.写入当前任务到已爬取库
        datum.incrExecuteCount(1);
        datum.setExecuteTime(System.currentTimeMillis());
        iDbWritor.writeFetchSegment(datum);

        //3.传输当前页面到解析模块
        IContentPageFilter.getContentPageData(responsePage);

        //4.解析响应页面的urls 并过滤
        CrawlDatums next = iSimpleParse.parseLinks(responsePage);
        if (next == null || next.isEmpty()) {
            log.info("当前页面未解析出后续任务");
        } else {
            //5.过滤解析的后续任务
            if (INextFilter != null) {
                next = INextFilter.filter(next);
            }
            //6.写入解析的后续任务，
            iDbWritor.writeParseSegment(next);
        }
        return (next == null ? new CrawlDatums() : next);
    }
}
