package parsercore.fetchercore;


import commoncore.entity.httpEntity.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import parsercore.fetchercore.generatorcore.IGenerator;

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
    private IGenerator<ResponseData> iGenerator;
    @Value(value = "${my.queue.maxSize}")
    private int queueMaxSize;
    @Value(value = "${my.responseList.redisKey}")
    private String redisKey;
    @Autowired
    private FetcherState fetcherState;

    /**
     * desc:关闭管道添加工具
     **/
    public void stopFeeder() {
        //停止数据库提取工具
        try {
            LOG.info("【正在关闭数据库提取工具】");
        } catch (Exception e) {
            LOG.error("【关闭数据库提取工具出错】");
        }
        fetcherState.setFeedRunnning(false);
    }

    /**
     * desc: feeder 线程运行
     **/
    @Override
    public void run() {
        LOG.info(iGenerator.toString());
        boolean hasMore = true;
        fetcherState.setFeedRunnning(true);
        //任务生产者依赖 数据库后续任务，自身状态，调度器状态
        while (hasMore && fetcherState.isFeedRunnning()&&fetcherState.isFetcherRunning()) {
            //监听queue中数量，当queue中数量为1000时，线程等待，
            int feed = queueMaxSize - queue.getSize();
            if (feed <= 0) {
                pause(1, 0);
                continue;
            }
            //如果queue中小于1000，往queue中添加新任务，未提取到任务 count 等待时间
            int count = 0;
            while (feed > 0 && hasMore && fetcherState.isFeedRunnning()) {
                //任务生成器 如果下一个任务为空，返回空。判断dbmananger中是否有后续任务
                ResponseData responseData = iGenerator.getData(redisKey);

                if (responseData != null) {
                    // pause(0, 100);
                    LOG.info("feed Size = " + feed);
                    LOG.info("queue size == " + queue.getSize());
                    queue.addResponseData(responseData);
                    feed--;
                } else {
                    pause(1, 0);
                    count++;
                    if (queue.getSize() == 0 && count >= 6) {
                        LOG.info("等待超时，关闭程序");
                        fetcherState.setFeedRunnning(false);
                        hasMore = false;
                    } else {
                        LOG.info("redis 中无后续数据，等待第 " + count + " 秒");
                    }
                }
            }
        }
    }

    public void pause(Integer seconds, Integer mills) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
            TimeUnit.MILLISECONDS.sleep(mills);
        } catch (InterruptedException e) {
            LOG.error("生产者休眠异常");
        }
    }
}
