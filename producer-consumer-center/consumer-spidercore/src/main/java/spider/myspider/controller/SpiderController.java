package spider.myspider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spider.myspider.SpiderEngine;


/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-24-14:39
 */
@RestController
public class SpiderController {
    @Autowired
    private SpiderEngine spiderEngine;

    @GetMapping(value = "/spider")
    public Boolean start() {
        new Thread(() -> spiderEngine.initToRun(), "startSpider--").start();
        return true;
    }

    @GetMapping(value = "/stop")
    public boolean stopspider() {
        new Thread(() -> spiderEngine.stopSpider(), "startSpider--").start();
        return true;
    }
}
