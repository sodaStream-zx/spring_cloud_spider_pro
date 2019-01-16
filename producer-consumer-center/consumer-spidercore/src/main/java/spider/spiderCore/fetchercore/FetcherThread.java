package spider.spiderCore.fetchercore;

import commoncore.customUtils.BeanGainer;
import commoncore.entity.fetcherEntity.FetcherState;
import commoncore.entity.requestEntity.CrawlDatum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import spider.spiderCore.entitys.CrawlDatums;
import spider.spiderCore.iexecutorCom.IExecutor;

import java.util.concurrent.TimeUnit;

/**
 * @author 一杯咖啡
 * @desc 任务消费者
 * @createTime
 */
@Component(value = "fetcherThread")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FetcherThread implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(FetcherThread.class);
    @Autowired
    private FetcherState fetcherState;
    @Autowired
    private FetchQueue fetchQueue;
    private long defaultExecuteInterval = 0;
    private IExecutor<CrawlDatums> iExecutor;

    public FetcherThread() {
        LOG.info("加载默认执行器");
        this.iExecutor = BeanGainer.getBean("defaultExecutor", IExecutor.class);
    }

    @Override
    public void run() {
        CrawlDatum datum;
        //判断调度器是否处于运行状态
        while (fetcherState.isFetcherRunning() || fetchQueue.getSize() > 0) {
            //从queue中取出任务
            datum = fetchQueue.getCrawlDatum();
            if (datum == null) {
                LOG.info("QUEUE 未取得任务");
                //判断任务队列是否有任务，如果没有，等待任务管道被消费完，停止执行线程
                if (fetcherState.isFeederRunnning()) {
                    pause(0, 200);
                    continue;
                } else {
                    break;
                }
            } else {
                LOG.debug("QUEUE 取得任务" + datum.getUrl());
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

    public long getDefaultExecuteInterval() {
        return defaultExecuteInterval;
    }

    public IExecutor<CrawlDatums> getiExecutor() {
        return iExecutor;
    }
}
