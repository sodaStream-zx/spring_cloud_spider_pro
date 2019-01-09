package parsercore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import parsercore.fetchercore.FetcherProcess;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-24-12:27
 */
@RestController
public class ParesController {

    @Autowired
    private FetcherProcess fetcherProcess;

    @GetMapping(value = "/startParse")
    public boolean startParer() {
        fetcherProcess.fetcherStart();
        return true;
    }
}
