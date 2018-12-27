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

    @GetMapping(value = "/spiderRun")
    public Boolean start() {
        return spiderEngine.initToRun();
    }

    public boolean stopspider() {

        return true;
    }
}
