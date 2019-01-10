package spider.spiderCore.ihttp.httpImp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spider.spiderCore.ihttp.IProxyGain;

import java.net.Proxy;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2019-01-02-11:10
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultProxyGainTest {
    @Autowired
    IProxyGain iProxyGain;

    @Test
    public void nextRandom() {
        iProxyGain.add("19.92.168.28:8080");
        iProxyGain.add("13.92.168.28:8081");
        iProxyGain.add("14.92.168.28:8082");
        for (int i = 0; i < 3; i++) {
            Proxy proxy = iProxyGain.nextRandom();
            System.out.println("prox " + proxy);
        }
    }

    @Test
    public void add() {
    }
}