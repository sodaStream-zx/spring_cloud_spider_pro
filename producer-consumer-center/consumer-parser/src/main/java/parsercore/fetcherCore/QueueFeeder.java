package parsercore.fetcherCore;


import commoncore.customUtils.SleepUtil;
import commoncore.entity.fetcherEntity.FetcherState;
import commoncore.entity.httpEntity.ParseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import parsercore.fetcherCore.generatorcore.IParseDataGenerator;

/**
 * @author 一杯咖啡
 * @desc 任务生产者
 * @createTime
 */
@Component
//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueueFeeder implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(QueueFeeder.class);
    private FetchQueue queue;
    private IParseDataGenerator<ParseData> iParseDataGenerator;
    private int queueMaxSize;
    private FetcherState fetcherState;

    @Autowired
    public QueueFeeder(FetchQueue queue,
                       IParseDataGenerator<ParseData> iParseDataGenerator,
                       FetcherState fetcherState,
                       @Value(value = "${my.queue.maxSize}") int queueMaxSize) {
        this.queue = queue;
        this.iParseDataGenerator = iParseDataGenerator;
        this.fetcherState = fetcherState;
        this.queueMaxSize = queueMaxSize;
    }

    /**
     * desc:关闭管道添加工具
     **/
    public boolean stopFeeder() {
        fetcherState.setFeederRunnning(false);
        SleepUtil.pause(2, 0);
        return !fetcherState.isFeederRunnning();
    }

    /**
     * desc: feeder 线程运行
     **/
    @Override
    public void run() {
        if (iParseDataGenerator == null) {
            LOG.error("未提供数据库提取工具，关闭提取线程");
            fetcherState.setFeederRunnning(false);
            return;
        }
        LOG.info(this.toString());
        boolean hasMore = true;
        fetcherState.setFeederRunnning(true);
        //任务生产者依赖 数据库后续任务，自身状态，调度器状态
        while (hasMore && fetcherState.isFeederRunnning() && fetcherState.isFetcherRunning()) {
            //监听queue中数量，当queue中数量为1000时，线程等待，
            int feed = queueMaxSize - queue.getSize();
            if (feed <= 0) {
                SleepUtil.pause(1, 0);
                continue;
            }
            //如果queue中小于1000，往queue中添加新任务，未提取到任务 count 等待时间
            while (feed > 0 && hasMore && fetcherState.isFeederRunnning() && fetcherState.isFetcherRunning()) {
                //任务生成器 如果下一个任务为空，返回空。判断dbmananger中是否有后续任务
                ParseData data = iParseDataGenerator.getData();
                if (data != null) {
                    // pause(0, 100);
                    //LOG.info("feed Size = " + feed + "；queuesize == " + queue.getSize());
                    queue.addResponseData(data);
                    feed--;
                } else {
                    int count = 0;
                    SleepUtil.pause(1, 0);
                    count++;
                    if (queue.getSize() == 0 && count >= 6) {
                        LOG.info("等待超时，关闭程序");
                        fetcherState.setFeederRunnning(false);
                        hasMore = false;
                    } else {
                        LOG.info("获取数据超时【" + count + "】秒");
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return " 提取线程 ：QueueFeeder [" +
                "\nqueue=" + queue +
                "\niParseDataGenerator=" + iParseDataGenerator +
                "\nqueueMaxSize=" + queueMaxSize +
                "\nfetcherState=" + fetcherState +
                ']';
    }
}
