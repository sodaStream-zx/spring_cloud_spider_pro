package maincore.controller;

import maincore.dao.ConfigFromMysqlToRedis;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-26-11:02
 */
@RestController
public class LoadConfigsController {
    private static final Logger LOG = Logger.getLogger(LoadConfigsController.class);
    @Autowired
    private ConfigFromMysqlToRedis configFromMysqlToRedis;

    /**
     * desc:加载配置文件到redis中
     **/
    @GetMapping(value = "/load")
    public boolean readConfigFormMysql() {
        configFromMysqlToRedis.MysqlWirteRedis();
        LOG.info("加载配置文件到redis 完成");
        return true;
    }
}
