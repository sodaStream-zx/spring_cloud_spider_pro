package parsercore.fetchercore;

import commoncore.customUtils.BeanGainer;
import commoncore.entity.fetcherEntity.FetcherState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 一杯咖啡
 * @desc 调度器，生产者，任务管道，消费者调度器
 * @createTime
 */
@Component
public class FetcherProcess {
    private static final Logger LOG = LoggerFactory.getLogger(FetcherProcess.class);

    /**
     * 核心组件:  fetchQueue-任务管道  queueFeeder-任务生产者
     */
    @Autowired
    private QueueFeeder queueFeeder;
    @Autowired
    private FetcherState fetcherState;
    @Value(value = "${my.fetchercore.maxThread}")
    private int threads;

    /**
     * 抓取当前所有任务，会阻塞到爬取完成 开启 feeder 和 执行爬取线程。
     *
     * @throws IOException 异常
     */
    public boolean fetcherStart() {
        //调度器开始，设置状态为 true
        fetcherState.setFetcherRunning(true);

        //创建线程池，允许核心线程超时关闭
        ThreadPoolExecutor threadsExecutor = new ThreadPoolExecutor(30, 45, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
        threadsExecutor.allowCoreThreadTimeOut(true);
        //任务生产者开始 ，添加上限1000个
        threadsExecutor.execute(queueFeeder);

        //等待管道先添加任务
        LOG.info(" 生产者先运行3秒，保证管道有数据足够多");
        pause(3, 0);
        //初始化消费者 从queue中读取任务
        for (int i = 0; i < threads; i++) {
            //动态获取bean ，使每个线程中的 执行单元独立。非共享单例
            threadsExecutor.execute(BeanGainer.getBean("fetcherThread", FetcherThread.class));
        }

        do {
            pause(1, 0);
            LOG.info("【线程池状态：\n" + threadsExecutor.toString() + " 】\n");
            //调度器线程 依赖执行线程运行数量 调度器状态，生产者状态
        }
        while (threadsExecutor.getActiveCount() > 0 && fetcherState.isFetcherRunning() && fetcherState.isFeederRunnning());

        this.stopFetcher();
        threadsExecutor.shutdown();
        LOG.info("线程池状态？？---" + threadsExecutor.toString());
        //LOG.info("清空管道 redis 可以考虑重新将未抓取的url存回redis中");
        //fetchQueue.clearQueue();
        return true;
    }

    /**
     * 停止爬取
     */
    public void stopFetcher() {
        //停止任务添加工具
        LOG.info("【----------停止任务生产者----------】");
        queueFeeder.stopFeeder();
        //停止调度器
        fetcherState.setFetcherRunning(false);
    }

    /**
     * desc: 线程休眠
     **/
    public void pause(int second, int mills) {
        try {
            TimeUnit.SECONDS.sleep(second);
            TimeUnit.MILLISECONDS.sleep(mills);
        } catch (InterruptedException e) {
            LOG.error("调度器休眠出错");
        }
    }
}
