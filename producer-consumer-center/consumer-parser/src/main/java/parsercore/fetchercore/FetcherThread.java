package parsercore.fetchercore;

import commoncore.entity.responseEntity.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsercore.pareser.IParesEngine;

import java.util.concurrent.TimeUnit;

/**
 * @author 一杯咖啡
 * @desc 任务消费者
 * @createTime
 */
public class FetcherThread implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(FetcherThread.class);
    private FetchQueue fetchQueue;
    private FetcherState fetcherState;
    private IParesEngine iParesEngine;

    public FetcherThread(FetchQueue fetchQueue, FetcherState fetcherState, IParesEngine iParesEngine) {
        this.fetchQueue = fetchQueue;
        this.fetcherState = fetcherState;
        this.iParesEngine = iParesEngine;
    }

    @Override
    public void run() {
        ResponseData responseData;
        //执行线程依赖 调度器 状态
        while (fetcherState.isFetcherRunning()) {
            //从queue中取出任务
            responseData = fetchQueue.getResponseData();
            if (responseData == null) {
                //判断任务队列是否有任务，如果没有，直接退出线程，只要 任务管道
                if (fetcherState.isFeedRunnning() || fetchQueue.getSize() > 0) {
                    continue;
                } else {
                    break;
                }
            } else {
                //LOG.info("item = "+rs.toString());
                //pause(8, 0);
                iParesEngine.parseRun(responseData);
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
