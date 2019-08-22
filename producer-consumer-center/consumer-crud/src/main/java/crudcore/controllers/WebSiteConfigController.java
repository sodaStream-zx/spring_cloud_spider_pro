package crudcore.controllers;

import commoncore.entity.loadEntity.WebSiteConf;
import crudcore.services.WebSiteConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author 一杯咖啡
 * @desc 网站配置 控制器
 * @createTime 2019-01-02-15:51
 */
@Controller
@RequestMapping(value = "/ws")
public class WebSiteConfigController {
    @Autowired
    private WebSiteConfigService webSiteConfigService;

    @GetMapping(value = "/findAll")
    public ModelAndView findAll() {
        ModelAndView modelAndView = new ModelAndView();
        List<WebSiteConf> wbs = webSiteConfigService.findAll();
        modelAndView.addObject("obList", wbs).setViewName("listPage");
        return modelAndView;
    }

    @GetMapping(value = "/find/{id}")
    public ModelAndView findById(@PathVariable(value = "id") int id) {
        ModelAndView modelAndView = new ModelAndView();
        WebSiteConf ws = webSiteConfigService.findById(id);
        modelAndView.addObject("ws", ws).setViewName("wsPage");
        return modelAndView;
    }
}
