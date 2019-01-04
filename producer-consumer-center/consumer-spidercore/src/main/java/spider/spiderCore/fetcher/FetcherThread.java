package spider.spiderCore.fetcher;

import commoncore.entity.requestEntity.CrawlDatum;
import commoncore.entity.requestEntity.CrawlDatums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spider.spiderCore.crawldb.IDataUtil;
import spider.spiderCore.fetcher.IFetcherTools.Executor;
import spider.spiderCore.fetcher.IFetcherTools.NextFilter;

import java.util.concurrent.TimeUnit;

/**
 * @author 一杯咖啡
 * @desc 任务消费者
 * @createTime
 */
@Component
public class FetcherThread implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(FetcherThread.class);
    @Autowired
    private FetcherState fetcherState;
    /**
     * 组件从fetcher中获取
     */
    @Autowired
    private Executor executor;
    @Autowired(required = false)
    private NextFilter nextFilter;
    @Autowired
    private FetchQueue fetchQueue;
    @Autowired
    private QueueFeeder queueFeeder;
    @Autowired
    private IDataUtil iDataUtil;

    private long defaultExecuteInterval = 0;
    /**
     * desc: 初始化执行线程
     **/
    public FetcherThread() {
    }

    @Override
    public void run() {
        CrawlDatum datum = null;
        //判断调度器是否处于运行状态
        while (fetcherState.isFetcherRunning()) {
            //从queue中取出任务
            datum = fetchQueue.getCrawlDatum();
            LOG.info("QUEUE 取得任务" + datum.url());
            if (datum == null) {
                //判断任务队列是否有任务，如果没有，直接退出线程，只要 任务管道
                if (fetcherState.isFeedRunnning() || fetchQueue.getSize() > 0) {
                    //pause(0, 500);
                    continue;
                } else {
                    break;
                }
            } else {
                CrawlDatums next = new CrawlDatums();
                try {
                    //调用函数执行分析当前页面，取出页面的urls 形成 next。
                    executor.execute(datum, next);
                    //过滤后续任务
                    if (nextFilter != null) {
                        CrawlDatums filteredNext = new CrawlDatums();
                        for (int i = 0; i < next.size(); i++) {
                            CrawlDatum filterResult = nextFilter.filter(next.get(i), datum);
                            if (filterResult != null) {
                                filteredNext.add(filterResult);
                            }
                        }
                        next = filteredNext;
                    }
                    LOG.info("done: " + datum.briefInfo());
                    //当前页面标记为 已爬取
                    datum.setStatus(CrawlDatum.STATUS_DB_SUCCESS);
                } catch (Exception ex) {
                    LOG.info("failed: " + datum.briefInfo(), ex);
                    datum.setStatus(CrawlDatum.STATUS_DB_FAILED);
                }
                datum.incrExecuteCount(1);
                datum.setExecuteTime(System.currentTimeMillis());
                try {
                    //写入当前任务到已抓取的任务库
                    iDataUtil.getIDbWritor().writeFetchSegment(datum);
                    LOG.info("写入已爬取" + datum.url());

                    if (datum.getStatus() == CrawlDatum.STATUS_DB_SUCCESS && !next.isEmpty()) {
                        //写入当前任务提取的符合条件的任务到 后续未抓取的任务库
                        iDataUtil.getIDbWritor().writeParseSegment(next);
                        LOG.info("写入hhouxu爬取" + next.size());
                    }
                } catch (Exception ex) {
                    LOG.info("Exception when updating db", ex);
                }

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
