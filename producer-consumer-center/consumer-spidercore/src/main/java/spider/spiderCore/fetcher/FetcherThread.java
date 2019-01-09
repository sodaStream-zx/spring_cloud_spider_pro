package spider.spiderCore.fetcher;

import commoncore.entity.requestEntity.CrawlDatum;
import commoncore.entity.requestEntity.CrawlDatums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import spider.spiderCore.crawler.IExecutor;

import java.util.concurrent.TimeUnit;

/**
 * @author 一杯咖啡
 * @desc 任务消费者
 * @createTime
 */
@Component
public class FetcherThread {
    private static final Logger LOG = LoggerFactory.getLogger(FetcherThread.class);
    @Autowired
    private FetcherState fetcherState;
    @Autowired
    private IExecutor<CrawlDatums> iExecutor;
    @Autowired
    private FetchQueue fetchQueue;
    private long defaultExecuteInterval = 0;

    @Async
    public void startFetcher() {
        CrawlDatum datum;
        //判断调度器是否处于运行状态
        while (fetcherState.isFetcherRunning()) {
            //从queue中取出任务
            datum = fetchQueue.getCrawlDatum();
            if (datum == null) {
                LOG.info("QUEUE 未取得任务");
                //判断任务队列是否有任务，如果没有，等待任务管道被消费完，停止执行线程
                if (fetcherState.isFeedRunnning() || fetchQueue.getSize() > 0) {
                    pause(0, 200);
                    continue;
                } else {
                    break;
                }
            } else {
                LOG.info("QUEUE 取得任务" + datum.url());
                //调用执行器
                iExecutor.execute(datum);
                //当前页面执行完毕，等待时间
                long executeInterval = defaultExecuteInterval;
                if (executeInterval > 0) {
                    pause(0, executeInterval);
                }
            }
        }
    }

    /**
     * desc: 线程休眠
     **/
    public void pause(int second, long mills) {
        try {
            TimeUnit.SECONDS.sleep(second);
            TimeUnit.MILLISECONDS.sleep(mills);
        } catch (InterruptedException e) {
            LOG.error("spinWaiting thread sleep exception");
        }
    }
}
