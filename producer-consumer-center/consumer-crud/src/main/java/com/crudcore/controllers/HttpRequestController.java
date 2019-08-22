package com.crudcore.controllers;

import commoncore.entity.HttpProperties;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * @author 一杯咖啡
 * @desc http头，post数据封装
 * @createTime 2019-01-02-15:06
 */
@RestController
@RequestMapping(value = "/pro")
public class HttpRequestController {
    @GetMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("indexpage");
        return modelAndView;
    }

    @PostMapping(value = "/set")
    public String getPeo(int[] user_agent, int[] cookie, int[] accept) {
        for (int i : user_agent) {
            System.out.print(i + ",");
        }
        System.out.println(" ");
        for (int i : cookie) {
            System.out.print(i + ",");
        }
        System.out.println(" ");
        for (int i : accept) {
            System.out.print(i + ",");
        }
        System.out.println(" ");
        return "ook";
    }

    @PostMapping(value = "/maps")
    public boolean setHeader(@RequestBody Map<String, String> header, Map<String, String> post) {
        //封装header
        MultiValueMap<String, String> headerMap = mapToMVMap(header);
        //封装 post
        MultiValueMap<String, String> postMap = mapToMVMap(post);
        HttpProperties httpProperties = new HttpProperties();
        httpProperties.setHeaderMap(headerMap);
        httpProperties.setPostBodyMap(postMap);
        //save into mysql
        System.out.println("Http = " + httpProperties.toString());
        return true;
    }

    public MultiValueMap mapToMVMap(Map<String, String> map) {
        MultiValueMap<String, String> mVMap = null;
        Set<String> keys = map.keySet();
        if (keys.size() > 0) {
            mVMap = new LinkedMultiValueMap<>();
            for (String key : keys) {
                String[] values = map.get(key).split(",");
                if (values.length > 0) {
                    for (String value : values) {
                        mVMap.add(key, value);
                    }
                }
            }
        }
        return mVMap;
    }

    @PostMapping(value = "/pos")
    public String get(HttpServletRequest request) {
        System.out.println("request.getRequestURL()" + request.getRequestURL());
        return "ok";
    }
}
