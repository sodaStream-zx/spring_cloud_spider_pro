package crudcore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-08-1:01
 */
@Controller
public class TestController {
    @GetMapping(value = "/index")
    public String index() {
        int i = 1 / 0;
        System.out.println("i" + i);
        return "index";
    }
}
