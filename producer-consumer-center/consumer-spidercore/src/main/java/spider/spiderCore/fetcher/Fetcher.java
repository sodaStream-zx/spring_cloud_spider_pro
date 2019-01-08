package spider.spiderCore.fetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spider.spiderCore.crawldb.IDataUtil;
import spider.spiderCore.crawler.IExecutor;
import spider.spiderCore.fetcher.IFetcherTools.INextFilter;

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

    /**
     * 核心组件:  fetchQueue-任务管道  queueFeeder-任务生产者
     */
    @Autowired
    private FetchQueue fetchQueue;
    @Autowired
    private QueueFeeder queueFeeder;
    @Autowired
    private FetcherState fetcherState;
    @Autowired
    private FetcherThread fetcherThread;
    /**
     * 外部注入
     */
    @Autowired
    private IDataUtil iDataUtil;
    @Autowired(required = false)
    private INextFilter INextFilter = null;

    @Autowired
    private IExecutor iExecutor;
    /**
     * 线程状态属性:  threads-线程数量  fetcherRuning-调度器状态
     */
    private int threads = 20;

    /**
     * 抓取当前所有任务，会阻塞到爬取完成 开启 feeder 和 执行爬取线程。
     *
     * @throws IOException 异常
     */
    public Integer fetcherStart() {
        if (iExecutor == null) {
            LOG.info("未提供任务执行器");
            return 0;
        }
        //合并 入口和解析 任务库到 运行任务库
        iDataUtil.getIDbManager().merge();
        try {
            iDataUtil.getIDbWritor().initSegmentWriter();
            LOG.info("数据库 工具" + iDataUtil.getClass().getName());
            fetcherState.setFetcherRunning(true);

            //等待管道先添加任务
            pause(3, 0);
            //创建线程池，允许核心线程超时关闭
            ThreadPoolExecutor threadsExecutor = new ThreadPoolExecutor(30, 45, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
            threadsExecutor.allowCoreThreadTimeOut(true);
            threadsExecutor.execute(queueFeeder);
            //初始化消费者 从queue中读取任务
            for (int i = 0; i < threads; i++) {
                threadsExecutor.execute(fetcherThread);
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
            LOG.info("线程池关闭？？---" + threadsExecutor.isTerminated());
            //清空管道 redis 可以考虑重新将未抓取的url存回redis中
            fetchQueue.clearQueue();
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
        //停止任务添加工具
        LOG.info("【----------停止任务生产者----------】");
        queueFeeder.stopFeeder();
        //停止调度器
        fetcherState.setFetcherRunning(false);
    }


    public FetchQueue getFetchQueue() {
        return fetchQueue;
    }

    public QueueFeeder getQueueFeeder() {
        return queueFeeder;
    }

    public INextFilter getINextFilter() {
        return INextFilter;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public IDataUtil getiDataUtil() {
        return iDataUtil;
    }

    public FetcherThread getFetcherThread() {
        return fetcherThread;
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
