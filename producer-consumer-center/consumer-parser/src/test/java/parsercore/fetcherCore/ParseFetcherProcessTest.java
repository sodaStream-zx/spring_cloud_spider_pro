package parsercore.fetcherCore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-26-17:17
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ParseFetcherProcessTest {
    @Autowired
    private ParseFetcherProcess parseFetcherProcess;

    @Test
    public void fetcherStart() {
        parseFetcherProcess.fetcherStart();
    }

    @Test
    public void stopFetcher() {
    }

    @Test
    public void pause() {
    }
}