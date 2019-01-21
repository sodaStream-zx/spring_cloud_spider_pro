package spider.spiderCore.fetchercore;

import commoncore.customUtils.BeanGainer;
import commoncore.customUtils.SleepUtil;
import commoncore.entity.fetcherEntity.FetcherState;
import commoncore.entity.requestEntity.CrawlDatum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import spider.spiderCore.entitys.CrawlDatums;
import spider.spiderCore.iexecutorCom.IExecutor;

/**
 * @author 一杯咖啡
 * @desc 任务消费者
 * @createTime
 */
@Component(value = "fetcherThread")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FetcherThread implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(FetcherThread.class);
    private FetcherState fetcherState;
    private FetchQueue fetchQueue;
    private long defaultExecuteInterval = 0;
    private IExecutor<CrawlDatums> iExecutor;

    @Autowired
    public FetcherThread(FetcherState fetcherState,
                         FetchQueue fetchQueue,
                         @Value(value = "${fetcher.executeInterval}") long defaultExecuteInterval) {
        this.fetcherState = fetcherState;
        this.fetchQueue = fetchQueue;
        this.defaultExecuteInterval = defaultExecuteInterval;
        LOG.info("加载默认执行器");
        this.iExecutor = BeanGainer.getBean("iExecutor", IExecutor.class);
    }

    @Override
    public void run() {
        CrawlDatum datum;
        //判断调度器是否处于运行状态
        while (fetcherState.isFetcherRunning() || fetchQueue.getSize() > 0) {
            //从queue中取出任务
            datum = fetchQueue.getCrawlDatum();
            if (datum == null) {
                LOG.warn("QUEUE中无后续任务");
                //判断生产者是否运行中，如果没有，等待任务管道被消费完，停止执行线程
                if (fetcherState.isFeederRunnning()) {
                    SleepUtil.pause(0, 200);
                    continue;
                } else {
                    break;
                }
            } else {
                LOG.debug("QUEUE 取得任务" + datum.getUrl());
                //调用执行器
                iExecutor.execute(datum);

                //当前页面执行完毕，间隔
                long executeInterval = defaultExecuteInterval;
                if (executeInterval > 0) {
                    SleepUtil.pause(0, executeInterval);
                }
            }
        }
    }

    public long getDefaultExecuteInterval() {
        return defaultExecuteInterval;
    }

    public IExecutor<CrawlDatums> getiExecutor() {
        return iExecutor;
    }
}
