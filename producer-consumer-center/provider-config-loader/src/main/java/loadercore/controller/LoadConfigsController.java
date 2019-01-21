package loadercore.controller;

import commoncore.entity.loadEntity.ConfigRedisKeys;
import loadercore.dao.ConfigFromMysqlToRedis;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 一杯咖啡
 * @desc 加载配置文件 controller
 * @createTime 2018-12-26-11:02
 */
@RestController
public class LoadConfigsController {
    private static final Logger LOG = Logger.getLogger(LoadConfigsController.class);
    @Autowired
    private ConfigFromMysqlToRedis configFromMysqlToRedis;
    @Autowired
    private ConfigRedisKeys configRedisKeys;

    /**
     * desc:加载配置文件到redis中
     **/
    @GetMapping(value = "/load")
    public boolean readConfigFormMysql() {
        configFromMysqlToRedis.mysqlWriteRedis();
        LOG.info("加载配置文件到redis 完成");
        return true;
    }

    @GetMapping(value = "/read")
    public boolean read() {
        configFromMysqlToRedis.readRedis(configRedisKeys.getWsConfHash());
        configFromMysqlToRedis.readRedis(configRedisKeys.getUrlRuleHash());
        configFromMysqlToRedis.readRedis(configRedisKeys.getDomainRuleHash());
        int i = 1 / 0;
        System.out.println("i" + i);
        return true;
    }
}
