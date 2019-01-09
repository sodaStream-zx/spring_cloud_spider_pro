package spider.spiderCore.fetcher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-10-0:08
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class FetcherTest {

    @Autowired
    Fetcher fetcher;

    @Test
    public void fetcherStart() {
        fetcher.fetcherStart();
    }
}