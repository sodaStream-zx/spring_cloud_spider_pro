package spider.spiderCore.http;

import commoncore.entity.responseEntity.ResponsePage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spider.spiderCore.http.httpImp.HttpRequest;


/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-27-17:10
 */

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class HttpRequestTest {
    @Autowired
    HttpRequest httpRequest;

    @Test
    public void responsePage() {
    }

    @Test
    public void response() {
        try {
            String url = "https://blog.csdn.net/fightingXia/article/details/71775516";
            ResponsePage httpResponse1 = httpRequest.responsePage();
            System.out.println("httpRequest" + httpRequest.toString());
        } catch (Exception e) {
        }
    }

    @Test
    public void config() {
    }
}
