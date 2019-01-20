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
import spider.spiderCore.iexecutorCom.IContentNeeded;
import spider.spiderCore.iexecutorCom.IExecutor;
import spider.spiderCore.iexecutorCom.INextLinksFilter;
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

    private IDbWritor iDbWritor;
    private ISendRequest<ResponseData> iSendRequest;
    private ISimpleParse<CrawlDatums, ResponseData> iSimpleParse;
    private IContentNeeded IContentNeeded;
    private INextLinksFilter INextLinksFilter;

    @Autowired
    public DefaultExecutor(IDbWritor iDbWritor) {
        this.iDbWritor = iDbWritor;
        log.info("加载默认执行器组件");
        this.iSendRequest = BeanGainer.getBean("defaultRequest", ISendRequest.class);
        this.iSimpleParse = BeanGainer.getBean("defaultSimpleParse", ISimpleParse.class);
        this.IContentNeeded = BeanGainer.getBean("defaultContentPageFilter", IContentNeeded.class);
        this.INextLinksFilter = BeanGainer.getBean("defaultNextFilter", INextLinksFilter.class);
        log.info("isendrequest:" + iSendRequest
                + "\nisimparse:" + iSimpleParse
                + "\nicontentPageFilter:" + IContentNeeded
                + "\ninextfilter:" + INextLinksFilter);
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

        IContentNeeded.getContentPageData(responseData);

        //4.解析响应页面的urls 并过滤
        CrawlDatums next = iSimpleParse.parseLinks(responseData);
        if (next == null || next.isEmpty()) {
            log.info("当前页面未解析出后续任务");
        } else {
            //5.过滤解析的后续任务
            if (INextLinksFilter != null) {
                next = INextLinksFilter.filter(next);
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
                ", IContentNeeded=" + IContentNeeded +
                ", INextLinksFilter=" + INextLinksFilter +
                '}';
    }
}
