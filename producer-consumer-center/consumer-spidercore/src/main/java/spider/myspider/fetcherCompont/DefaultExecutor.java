package spider.myspider.fetcherCompont;

import commoncore.customUtils.BeanGainer;
import commoncore.entity.httpEntity.ResponseData;
import commoncore.entity.requestEntity.CrawlDatum;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import spider.spiderCore.entitys.CrawlDatums;
import spider.spiderCore.idbcore.IDbWritor;
import spider.spiderCore.iexecutorCom.IContentNeed;
import spider.spiderCore.iexecutorCom.IExecutor;
import spider.spiderCore.iexecutorCom.INextFilter;
import spider.spiderCore.iexecutorCom.ISimpleParse;
import spider.spiderCore.ihttp.ISendRequest;


/**
 * @author Twilight
 * @desc 默认执行器
 * @createTime 2019-01-07-15:35
 */
@Component(value = "iExecutor")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultExecutor implements IExecutor<CrawlDatums> {
    private static final Logger log = Logger.getLogger(DefaultExecutor.class);
    @Autowired
    private IDbWritor iDbWritor;
    private ISendRequest<ResponseData> iSendRequest;
    private ISimpleParse<CrawlDatums, ResponseData> iSimpleParse;
    private IContentNeed IContentNeed;
    private INextFilter INextFilter;

    public DefaultExecutor() {
        log.info("加载默认执行器组件");
        this.iSendRequest = BeanGainer.getBean("defaultRequest", ISendRequest.class);
        this.iSimpleParse = BeanGainer.getBean("defaultSimpleParse", ISimpleParse.class);
        this.IContentNeed = BeanGainer.getBean("defaultContentPageFilter", IContentNeed.class);
        this.INextFilter = BeanGainer.getBean("defaultNextFilter", INextFilter.class);
        log.info("isendrequest:" + iSendRequest
                + "\nisimparse:" + iSimpleParse
                + "\nicontentPageFilter:" + IContentNeed
                + "\ninextfilter:" + INextFilter);
    }

    @Override
    public CrawlDatums execute(CrawlDatum datum) {
        ResponseData responseData = null;
        try {
            //1.调用请求工具获取响应页面
            responseData = iSendRequest.converterResponsePage(datum);
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

        IContentNeed.getContentPageData(responseData);

        //4.解析响应页面的urls 并过滤
        CrawlDatums next = iSimpleParse.parseLinks(responseData);
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

    @Override
    public String toString() {
        return "DefaultExecutor{" +
                "iDbWritor=" + iDbWritor +
                ", iSendRequest=" + iSendRequest +
                ", iSimpleParse=" + iSimpleParse +
                ", IContentNeed=" + IContentNeed +
                ", INextFilter=" + INextFilter +
                '}';
    }
}
