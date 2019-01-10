package spider.spiderCore.fetchercore;


import commoncore.entity.fetcherEntity.FetcherState;
import commoncore.entity.requestEntity.CrawlDatum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spider.spiderCore.idbcore.IGenerator;

import java.util.concurrent.TimeUnit;

/**
 * @author 一杯咖啡
 * @desc 任务生产者
 * @createTime
 */
@Component
public class QueueFeeder implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(QueueFeeder.class);
    @Autowired
    private FetchQueue queue;
    @Autowired
    private IGenerator<CrawlDatum> iGenerator;
    //queeue 大小最大值设置
    private int queueMaxSize = 1000;
    @Autowired
    private FetcherState fetcherState;

    /**
     * desc:关闭管道添加工具
     **/
    public void stopFeeder() {
        //停止数据库提取工具
        try {
            LOG.info("【正在关闭数据库提取工具】");
            this.closeGenerator();
        } catch (Exception e) {
            LOG.error("【关闭数据库提取工具出错】");
        }
        fetcherState.setFeederRunnning(false);
    }

    /**
     * desc:关闭任务生成工具
     **/
    public void closeGenerator() {
        if (iGenerator != null) {
            iGenerator.close();
            LOG.info("close abstractGenerator:" + iGenerator.getClass().getName() + " ......");
        }
    }

    /**
     * desc: feeder 线程运行
     **/
    @Override
    public void run() {
        //获取任务生成工具 （从数据库中提取数据）
        LOG.info(iGenerator.toString());
        boolean hasMore = true;
        fetcherState.setFeederRunnning(true);

        while (hasMore && fetcherState.isFeederRunnning()) {
            //监听queue中数量，当queue中数量为1000时，线程等待，
            int feed = queueMaxSize - queue.getSize();
            if (feed <= 0) {
                pause(1, 0);
                continue;
            }
            //如果queue中小于1000，往queue中添加新任务，未提取到任务 count 等待时间
            int count = 0;
            while (feed > 0 && hasMore && fetcherState.isFeederRunnning()) {
                //任务生成器 如果下一个任务为空，返回空。判断dbmananger中是否有后续任务
                CrawlDatum datum = iGenerator.next();
                // CrawlDatum datum = new CrawlDatum("testUrl");
                if (datum != null) {
                    LOG.info("已提取数据" + datum.url());
                    queue.addCrawlDatum(datum);
                    feed--;//一直填到queue为1000
                } else {
                    LOG.info("未取得数据");
                    pause(1, 0);
                    count++;
                    if (count < 5) {
                        LOG.info("redis 中无后续任务，等待第 " + count + " 秒");
                    } else {
                        LOG.info("等待超时，关闭程序");
                        fetcherState.setFeederRunnning(false);
                        hasMore = false;
                    }
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
