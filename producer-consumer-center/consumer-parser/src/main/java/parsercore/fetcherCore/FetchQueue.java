package parsercore.fetcherCore;

import commoncore.entity.httpEntity.ParseData;
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

    public final List<ParseData> queue = Collections.synchronizedList(new LinkedList<ParseData>());

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
    public synchronized void addResponseData(ParseData rp) {
        if (rp == null) {
            return;
        }
        queue.add(rp);
        totalSize.incrementAndGet();
    }

    /**
     * desc:从queue中提取任务
     **/
    public synchronized ParseData getResponseData() {
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
            ParseData it = queue.get(i);
            LOG.info("  " + i + ". " + it.getPageUrl());
        }
    }
}
