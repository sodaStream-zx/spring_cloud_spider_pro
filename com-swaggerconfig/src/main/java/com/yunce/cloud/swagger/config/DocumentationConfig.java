package com.yunce.cloud.swagger.config;

import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zxx
 * @desc 网关swagger doc 配置 ---等待集成到swagger jar中，自动识别是否为网关服务
 * @createTime 2019-02-22-上午 11:03
 */


public class DocumentationConfig implements SwaggerResourcesProvider {

    private ZuulProperties zuulProperties;

    public DocumentationConfig(ZuulProperties zuulProperties) {
        this.zuulProperties = zuulProperties;
    }

    //配置路由服务接口地址
    @Override
    public List<SwaggerResource> get() {
        List resources = new ArrayList<>();
//        /usercenter/是网关路由，/v2/api-docs是swagger中的
        zuulProperties.getRoutes().
                forEach((s, zuulRoute) ->
                        resources.add(swaggerResource(s, zuulProperties.getPrefix() + zuulRoute.getPath().replace("**", "") + "v2/api-docs", "v1.0")));
        return resources;
    }

    //构造服务interfaces
    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }

}