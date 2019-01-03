package spider.myspider.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spider.myspider.SpiderEngine;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-27-16:29
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SpiderControllerTest {

    @Autowired
    SpiderEngine spiderEngine;
    @Test
    public void start() {
        //   spiderEngine.initToRun();
    }
}