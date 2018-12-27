package parsercore.fetchercore;

import commoncore.entity.responseEntity.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 一杯咖啡
 * @desc 任务缓存管道
 * @createTime
 */
@Component
public class FetchQueue {
    private static final Logger LOG = LoggerFactory.getLogger(FetchQueue.class);

    public AtomicInteger totalSize = new AtomicInteger(0);

    public final List<ResponseData> queue = Collections.synchronizedList(new LinkedList<ResponseData>());

    public void clearQueue() {
        queue.clear();
    }

    public int getSize() {
        return queue.size();
    }

    /**
     * desc:添加任务到queue中
     *
     * @Return: void
     **/
    public synchronized void addResponseData(ResponseData rp) {
        if (rp == null) {
            return;
        }
        queue.add(rp);
        totalSize.incrementAndGet();
    }

    /**
     * desc:从queue中提取任务
     **/
    public synchronized ResponseData getResponseData() {
        if (queue.isEmpty()) {
            return null;
        }
        return queue.remove(0);
    }

    /**
     * desc:打印queue中的任务
     **/
    public synchronized void dump() {
        for (int i = 0; i < queue.size(); i++) {
            ResponseData it = queue.get(i);
            LOG.info("  " + i + ". " + it.getDatum().url());
        }
    }

}
