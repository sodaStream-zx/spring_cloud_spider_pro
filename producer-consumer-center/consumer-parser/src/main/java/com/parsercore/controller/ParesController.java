package com.parsercore.controller;

import com.parsercore.fetcherCore.ParseFetcherProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-24-12:27
 */
@RestController
public class ParesController {

    @Autowired
    private ParseFetcherProcess parseFetcherProcess;

    @GetMapping(value = "/parse")
    public boolean startParse() {
        new Thread(() -> {
            parseFetcherProcess.fetcherStart();
        }, "stopThread--").start();
        return true;
    }

    @GetMapping(value = "/stop")
    public boolean stopParse() {
        new Thread(() -> parseFetcherProcess.stopFetcher(),
                "stopThread").start();
        return true;
    }
}
