package spider.spiderCore.fetchercore;

import commoncore.entity.requestEntity.CrawlDatum;
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
        fetchQueue.addCrawlDatum(new CrawlDatum("12412541252"));
        fetchQueue.addCrawlDatum(new CrawlDatum("1s412541152"));
        fetchQueue.addCrawlDatum(new CrawlDatum("124125412"));
        fetchQueue.addCrawlDatum(new CrawlDatum("1f41w541252"));
        fetchQueue.addCrawlDatum(new CrawlDatum("12etq412541252"));
        fetchQueue.dump();
    }
}