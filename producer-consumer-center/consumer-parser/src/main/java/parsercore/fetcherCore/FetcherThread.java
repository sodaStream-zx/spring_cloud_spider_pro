package parsercore.fetcherCore;

import commoncore.customUtils.BeanGainer;
import commoncore.entity.fetcherEntity.FetcherState;
import commoncore.entity.httpEntity.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import parsercore.parseExecutor.DefaultParesExecutor;
import parsercore.parseExecutor.IParesExecutor;

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
    private FetchQueue fetchQueue;
    @Autowired
    private FetcherState fetcherState;
    private IParesExecutor iParesExecutor;

    /**
     * desc: 实例化时，动态获取解析器
     **/
    public FetcherThread() {
        this.iParesExecutor = BeanGainer.getBean("paresExecutor", DefaultParesExecutor.class);
    }

    @Override
    public void run() {
        LOG.info("执行线程组件：" + iParesExecutor.getClass());
        ResponseData responseData;
        //执行线程依赖 调度器 状态，管道任务数量，只要有任务，或者 提取线程工作，者继续执行
        while (fetcherState.isFetcherRunning() || fetchQueue.getSize() > 0) {
            //从queue中取出任务
            responseData = fetchQueue.getResponseData();
            if (responseData == null) {
                //判断任务队列是否有任务，如果没有,直接退出
                if (fetcherState.isFeederRunnning() || fetchQueue.getSize() > 0) {
                    pause(0, 500);
                    continue;
                } else {
                    break;
                }
            } else {
                iParesExecutor.parseRun(responseData);
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
