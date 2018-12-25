package parsercore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import parsercore.pareser.IParesEngine;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-24-12:27
 */
@RestController
public class ParesController {

    @Autowired
    private IParesEngine iParesEngine;

    @GetMapping(value = "/startPares")
    public boolean startParer() {
        iParesEngine.paresRun();
        return true;
    }
}
