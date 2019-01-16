package spider.spiderCore.fetchercore;

import commoncore.customUtils.BeanGainer;
import commoncore.entity.fetcherEntity.FetcherState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spider.spiderCore.idbcore.IDataUtil;

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
public class Fetcher {
    private static final Logger LOG = LoggerFactory.getLogger(Fetcher.class);
    @Autowired
    private FetchQueue fetchQueue;
    @Autowired
    private QueueFeeder queueFeeder;
    @Autowired
    private FetcherState fetcherState;
    @Autowired
    private IDataUtil iDataUtil;
    @Value(value = "${spider.totalThreads}")
    private int threads;

    /**
     * 抓取当前所有任务，会阻塞到爬取完成 开启 feeder 和 执行爬取线程。
     *
     * @throws IOException 异常
     */
    public Integer fetcherStart() {
        //合并 入口和解析 任务库到 运行任务库
        iDataUtil.getIDbManager().merge();
        try {
            iDataUtil.getIDbWritor().initSegmentWriter();
            LOG.info("数据库 工具" + iDataUtil.getClass().getName());
            fetcherState.setFetcherRunning(true);
            //创建线程池，允许核心线程超时关闭
            ThreadPoolExecutor threadsExecutor = new ThreadPoolExecutor(threads, threads + (threads / 2), 2, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
            threadsExecutor.allowCoreThreadTimeOut(true);
            //任务生产者开始 ，添加上限1000个
            threadsExecutor.execute(queueFeeder);
            //等待管道先添加任务
            pause(2, 0);
            //初始化消费者 从queue中读取任务
            for (int i = 0; i < threads; i++) {
                threadsExecutor.execute(BeanGainer.getBean("fetcherThread", FetcherThread.class));
            }
            //主线程循环打印 线程池状态
            do {
                pause(1, 0);
                LOG.info("执行器状态:\n" + fetcherState.toString());
                LOG.info("【线程池状态：\n" + threadsExecutor.toString() + " 】\n");
            } while (threadsExecutor.getActiveCount() > 0 && fetcherState.isFetcherRunning());

            //立即停止任务添加到管道
            LOG.info("本地管道数量：" + fetchQueue.getSize());
            this.stopFetcher();
            threadsExecutor.shutdown();
            LOG.info("线程池状态？？---" + threadsExecutor.toString());
            LOG.info("线程池关闭？" + (threadsExecutor.isTerminated() ? "已关闭" : "未关闭"));
        } finally {
            if (queueFeeder != null) {
                queueFeeder.closeGenerator();
            }
            iDataUtil.getIDbWritor().closeSegmentWriter();
            LOG.info("close segmentWriter:" + iDataUtil.getIDbWritor().getClass().getName());
        }
        //返回生成的任务总数
        return iDataUtil.getIGenerator().totalGeneretNum();
    }

    /**
     * 停止爬取
     */
    public void stopFetcher() {
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

    public FetchQueue getFetchQueue() {
        return fetchQueue;
    }

    public QueueFeeder getQueueFeeder() {
        return queueFeeder;
    }

    public FetcherState getFetcherState() {
        return fetcherState;
    }

    public IDataUtil getiDataUtil() {
        return iDataUtil;
    }
}
