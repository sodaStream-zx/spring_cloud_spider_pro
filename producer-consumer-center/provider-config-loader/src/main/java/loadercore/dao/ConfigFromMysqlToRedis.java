
package loadercore.dao;

import commoncore.customUtils.SerializeUtil;
import commoncore.customUtils.SleepUtil;
import commoncore.entity.loadEntity.*;
import commoncore.entity.loadEntity.jpaDao.DomainRuleJpaDao;
import commoncore.entity.loadEntity.jpaDao.SiteConfigJpaDao;
import commoncore.entity.loadEntity.jpaDao.UrlRuleJpaDao;
import commoncore.entity.loadEntity.jpaDao.WebSiteConfJpaDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * <p>项目名称: ${小型分布式爬虫} </p>
 * <p>文件名称: ${MysqlToRedis} </p>
 * <p>描述: [读取mysql 网站配置信息到redis队列] </p>
 *
 * @author 一杯咖啡
 */

@Service
public class ConfigFromMysqlToRedis {

    private static final Logger LOG = Logger.getLogger(ConfigFromMysqlToRedis.class);
    private RedisTemplate redisTemplate;
    private SiteConfigJpaDao siteConfigJpaDao;
    private WebSiteConfJpaDao webSiteConfJpaDao;
    private UrlRuleJpaDao urlRuleJpaDao;
    private DomainRuleJpaDao domainRuleJpaDao;
    private ConfigRedisKeys configRedisKeys;

    @Autowired
    public ConfigFromMysqlToRedis(RedisTemplate redisTemplate,
                                  SiteConfigJpaDao siteConfigJpaDao,
                                  WebSiteConfJpaDao webSiteConfJpaDao,
                                  UrlRuleJpaDao urlRuleJpaDao,
                                  DomainRuleJpaDao domainRuleJpaDao,
                                  ConfigRedisKeys configRedisKeys) {
        this.redisTemplate = redisTemplate;
        this.siteConfigJpaDao = siteConfigJpaDao;
        this.webSiteConfJpaDao = webSiteConfJpaDao;
        this.urlRuleJpaDao = urlRuleJpaDao;
        this.domainRuleJpaDao = domainRuleJpaDao;
        this.configRedisKeys = configRedisKeys;
    }

    /**
     * desc:主节点需要该功能从mysql数据库读到redis队列
     **/
    @Transactional
    public void mysqlWriteRedis() {
        //List<SiteConfig> scs = sitesConfigJTDao.Read();

        List<SiteConfig> list = siteConfigJpaDao.findAll();
        list.stream()
                .map(SerializeUtil::serializeToString)
                .forEach(x -> redisTemplate.opsForList().leftPush("sites", x.get()));
        SleepUtil.pause(1, 0);
        this.loadWebConfig();
        this.loadUrlRule();
        this.loadDomainRule();
    }

    /**
     * desc: 加载网站配置 入口等
     **/
    public void loadWebConfig() {
        LOG.debug("加载网站规则...");
        List<WebSiteConf> webs = webSiteConfJpaDao.findAll();
        if (webs.size() > 0) {
            for (WebSiteConf ws : webs) {
                Optional<String> wsStr = SerializeUtil.serializeToString(ws);
                redisTemplate.opsForHash().put(configRedisKeys.getWsConfHash(), ws.getSiteName(), wsStr.get());
            }
        }
    }

    /**
     * desc:加载网站url解析规则配置
     **/
    public void loadUrlRule() {
        LOG.debug("加载url解析规则...");
        List<UrlRule> rules = urlRuleJpaDao.findAll();
        if (rules.size() > 0) {
            for (UrlRule ur : rules) {
                Optional<String> urStr = SerializeUtil.serializeToString(ur);
                redisTemplate.opsForHash().put(configRedisKeys.getUrlRuleHash(), ur.getSiteName(), urStr.get());
            }
        }
    }

    /**
     * desc:加载网站解析器配置
     **/
    public void loadDomainRule() {
        LOG.debug("加载正文解析规则...");
        List<DomainRule> doRs = domainRuleJpaDao.findAll();
        if (doRs.size() > 0) {
            for (DomainRule dr : doRs) {
                Optional<String> drStr = SerializeUtil.serializeToString(dr);
                redisTemplate.opsForHash().put(configRedisKeys.getDomainRuleHash(), dr.getSiteName(), drStr.get());
            }
        }
    }

    /**
     * desc:从redis中读取配置
     **/
    public void readRedis(String dbKey) {
        List<String> configs = redisTemplate.opsForHash().values(dbKey);
        LOG.info(configs.size());
        configs.stream()
                .map(SerializeUtil::deserializeToObject)
                .forEach(x -> System.out.println(x.get().toString()));
    }
}

