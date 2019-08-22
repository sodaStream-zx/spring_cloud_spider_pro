package com.loadercore.controller;

import com.loadercore.dao.ConfigToRedis;
import commoncore.entity.loadEntity.RedisDbKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 一杯咖啡
 * @desc 加载配置文件 controller
 * @createTime 2018-12-26-11:02
 */
@RestController
public class LoadConfigsController {
    private static final Logger LOG = LoggerFactory.getLogger(LoadConfigsController.class);
    @Autowired
    private ConfigToRedis configToRedis;
    @Autowired
    private RedisDbKeys redisDbKeys;

    /**
     * desc:加载配置文件到redis中
     **/
    @GetMapping(value = "/load")
    public boolean readConfigFormMysql() {
        configToRedis.mysqlWriteRedis();
        LOG.info("加载配置文件到redis 完成");
        return true;
    }

    @GetMapping(value = "/read")
    public Map read() {
        HashMap<String, String> myMap = new HashMap<>();
        List<String> webConfig = configToRedis.readForList(redisDbKeys.getWsConfList());
        if (webConfig != null) {
            webConfig.stream().forEach(x -> myMap.put(redisDbKeys.getWsConfList(), x));
        }
        Map<String, String> urlConfigs = configToRedis.readForHashMap(redisDbKeys.getUrlRuleHash());
        if (urlConfigs != null) {
            urlConfigs.forEach(myMap::put);
        }
        Map<String, String> doConfigs = configToRedis.readForHashMap(redisDbKeys.getDomainRuleHash());
        if (doConfigs != null) {
            doConfigs.forEach(myMap::put);
        }
        return myMap;
    }

    @GetMapping(value = "/getRedisConfig")
    public RedisDbKeys getRedisDbKeys() {
        return redisDbKeys;
    }
}
