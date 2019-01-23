package spider.spiderCore.fetchercore;

import commoncore.entity.requestEntity.FetcherTask;
import org.junit.Test;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-08-11:03
 */
public class FetchQueueTest {
    FetchQueue fetchQueue = new FetchQueue();

    @Test
    public void dump() {
        fetchQueue.addTask(new FetcherTask("12412541252"));
        fetchQueue.addTask(new FetcherTask("1s412541152"));
        fetchQueue.addTask(new FetcherTask("124125412"));
        fetchQueue.addTask(new FetcherTask("1f41w541252"));
        fetchQueue.addTask(new FetcherTask("12etq412541252"));
        fetchQueue.dump();
    }
}