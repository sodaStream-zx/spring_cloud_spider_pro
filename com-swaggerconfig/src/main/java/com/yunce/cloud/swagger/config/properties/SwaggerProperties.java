package com.yunce.cloud.swagger.config.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.Serializable;

/**
 * @author zxx
 * @desc swagger 配置文档信息
 * @createTime 2019-02-22-下午 2:20
 */
//@Component
@RefreshScope
@ConfigurationProperties(value = "swagger.config")
public class SwaggerProperties implements Serializable {
    private static final long serialVersionUID = 8294103005L;
    @Autowired
    private Environment environment;
    private String appName;
    private String ipAddress;
    private String appPort;
    private String packLocation;//controller 路径
    private String desc = "description ";//文档描述
    private String contact = "contact";//创建者信息
    private String version = "dev";//版本号

    @PostConstruct
    public void init() {
        this.overrideFromEnv(this.environment);
    }

    /**
     * @param env 应用环境
     * @desc: 初始化，从环境中读取值，覆盖默认值
     */
    public void overrideFromEnv(Environment env) {
        if (StringUtils.isEmpty(this.getPackLocation())) {
            this.setPackLocation(env.resolvePlaceholders("${swagger.config.controller-location:}"));
        }
        if (StringUtils.isEmpty(this.getAppName())) {
            this.setAppName(env.resolvePlaceholders("${spring.application.name:}"));
        }
        if (StringUtils.isEmpty(this.getIpAddress())) {
            this.setIpAddress(env.resolvePlaceholders("${spring.cloud.client.ip-address:}"));
        }
        if (StringUtils.isEmpty(this.getAppPort())) {
            this.setAppPort(env.resolvePlaceholders("${server.port:}"));
        }
        if (StringUtils.isEmpty(this.getDesc())) {
            this.setDesc(env.resolvePlaceholders("${swagger.config.desc:}"));
        }

        if (StringUtils.isEmpty(this.getContact())) {
            this.setContact(env.resolvePlaceholders("${swagger.config.contact:}"));
        }

        if (StringUtils.isEmpty(this.getVersion())) {
            this.setVersion(env.resolvePlaceholders("${swagger.config.version:}"));
        }
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackLocation() {
        return packLocation;
    }

    public void setPackLocation(String packLocation) {
        this.packLocation = packLocation;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getAppPort() {
        return appPort;
    }

    public void setAppPort(String appPort) {
        this.appPort = appPort;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "SwaggerProperties{" +
                "appName='" + appName + '\'' +
                ", packLocation='" + packLocation + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", appPort='" + appPort + '\'' +
                ", desc='" + desc + '\'' +
                ", contact='" + contact + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
