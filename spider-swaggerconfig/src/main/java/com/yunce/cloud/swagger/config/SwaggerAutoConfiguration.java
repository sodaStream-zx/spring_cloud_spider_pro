package com.yunce.cloud.swagger.config;

import com.yunce.cloud.swagger.config.properties.SwaggerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.ZuulProxyMarkerConfiguration;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author zxx
 * @desc autoconfig swagger
 * @createTime 2019-02-22-下午 12:52
 */
@EnableSwagger2
@EnableConfigurationProperties(value = SwaggerProperties.class)
public class SwaggerAutoConfiguration {
    @Autowired
    SwaggerProperties swaggerProperties;

    @RefreshScope
    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(swaggerProperties.getAppName() + ":服务接口文档")
                .description(swaggerProperties.getDesc())
                .termsOfServiceUrl("http://" + swaggerProperties.getIpAddress() + ":" + swaggerProperties.getAppPort() + "/")
                .contact(swaggerProperties.getContact())
                .version(swaggerProperties.getVersion())
                .build();
    }

    @Bean
    @Autowired
    public Docket createRestApi(ApiInfo apiInfo) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getPackLocation()))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    @ConditionalOnBean(value = ZuulProxyMarkerConfiguration.class)
    @Primary
    @RefreshScope
    public DocumentationConfig gatewaySwagger(ZuulProperties zuulProperties) {
        return new DocumentationConfig(zuulProperties);
    }

    @Bean
    @ConditionalOnBean(value = ZuulProxyMarkerConfiguration.class)
    UiConfiguration uiConfig() {
        return new UiConfiguration("/", "list", "alpha", "schema",
                UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, true, 60000L);
    }
}
