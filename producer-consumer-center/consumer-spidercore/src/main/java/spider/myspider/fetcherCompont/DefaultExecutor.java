package spider.myspider.fetcherCompont;

import commoncore.customUtils.BeanGainer;
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

import java.util.concurrent.TimeUnit;


/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-07-15:35
 */
@Component(value = "defaultExecutor")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultExecutor implements IExecutor<CrawlDatums> {
    private static final Logger log = Logger.getLogger(DefaultExecutor.class);
    @Autowired
    private IDbWritor iDbWritor;

    private ISendRequest<ResponsePage> iSendRequest;
    private ISimpleParse iSimpleParse;
    private IContentPageFilter IContentPageFilter;
    private INextFilter INextFilter;

    public DefaultExecutor() {
        this.iSendRequest = BeanGainer.getBean("defaultRequest", ISendRequest.class);
        this.iSimpleParse = BeanGainer.getBean("defaultSimpleParse", ISimpleParse.class);
        this.IContentPageFilter = BeanGainer.getBean("defaultContentPageFilter", IContentPageFilter.class);
        this.INextFilter = BeanGainer.getBean("defaultNextFilter", INextFilter.class);
    }

    /* @Override
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
     }*/

    @Override
    public CrawlDatums execute(CrawlDatum datum) {
        log.info("模拟解析" + datum.url());
        log.info("执行器组件:" + this.toString());
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "DefaultExecutor{" +
                "iDbWritor=" + iDbWritor +
                ", iSendRequest=" + iSendRequest +
                ", iSimpleParse=" + iSimpleParse +
                ", IContentPageFilter=" + IContentPageFilter +
                ", INextFilter=" + INextFilter +
                '}';
    }
}
