
package loadercore.dao;

import commoncore.customUtils.SerializeUtil;
import commoncore.entity.loadEntity.DomainRule;
import commoncore.entity.loadEntity.RedisDbKeys;
import commoncore.entity.loadEntity.UrlRule;
import commoncore.entity.loadEntity.WebSiteConf;
import commoncore.entity.loadEntity.jpaDao.DomainRuleJpaDao;
import commoncore.entity.loadEntity.jpaDao.RedisDbKeysJpaDao;
import commoncore.entity.loadEntity.jpaDao.UrlRuleJpaDao;
import commoncore.entity.loadEntity.jpaDao.WebSiteConfJpaDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * <p>项目名称: ${小型分布式爬虫} </p>
 * <p>文件名称: ${MysqlToRedis} </p>
 * <p>描述: [读取mysql 网站配置信息到redis队列] </p>
 *
 * @author 一杯咖啡
 */

@Service
public class ConfigToRedis {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigToRedis.class);
    private RedisTemplate redisTemplate;
    private WebSiteConfJpaDao webSiteConfJpaDao;
    private UrlRuleJpaDao urlRuleJpaDao;
    private DomainRuleJpaDao domainRuleJpaDao;
    private RedisDbKeysJpaDao redisDbKeysJpaDao;
    private RedisDbKeys redisDbKeys;
    @Value(value = "${redisDb.config}")
    private String redisKeyConfig;

    @Autowired
    public ConfigToRedis(RedisTemplate redisTemplate,
                         WebSiteConfJpaDao webSiteConfJpaDao,
                         UrlRuleJpaDao urlRuleJpaDao,
                         DomainRuleJpaDao domainRuleJpaDao,
                         RedisDbKeys redisDbKeys,
                         RedisDbKeysJpaDao redisDbKeysJpaDao) {
        this.redisTemplate = redisTemplate;
        this.webSiteConfJpaDao = webSiteConfJpaDao;
        this.urlRuleJpaDao = urlRuleJpaDao;
        this.domainRuleJpaDao = domainRuleJpaDao;
        this.redisDbKeys = redisDbKeys;
        this.redisDbKeysJpaDao = redisDbKeysJpaDao;
    }


    /**
     * desc:主节点需要该功能从mysql数据库读到redis队列
     **/
    public void mysqlWriteRedis() {
        //List<SiteConfig> scs = sitesConfigJTDao.Read();
       /* List<SiteConfig> list = siteConfigJpaDao.findAll();
        list.stream()
                .map(SerializeUtil::serializeToString)
                .forEach(x -> redisTemplate.opsForList().leftPush("sites", x.get()));
        SleepUtil.pause(1, 0);*/
        this.loadRedisKeys();
        this.loadWebConfig();
        this.loadUrlRule();
        this.loadDomainRule();
    }

    /**
     * desc:加载redis数据表约定
     **/
    public void loadRedisKeys() {
        //1.从mysql中读取配置
        RedisDbKeys mrd = redisDbKeysJpaDao.findAll().get(0);
        redisDbKeys.configOwn(mrd);
        //2.写入配置到redis中
        Optional<String> redisDKStr = SerializeUtil.serializeToString(redisDbKeys);
        redisTemplate.opsForValue().set(redisKeyConfig, redisDKStr.get());
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
                redisTemplate.opsForList().leftPush(redisDbKeys.getWsConfList(), wsStr.get());
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
                // redisTemplate.opsForHash().put(redisDbKeys.getUrlRuleHash(), ur.getSiteName(), urStr.get());
                redisTemplate.opsForHash().putIfAbsent(redisDbKeys.getUrlRuleHash(), ur.getSiteName(), urStr.get());
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
                // redisTemplate.opsForHash().put(redisDbKeys.getDomainRuleHash(), dr.getSiteName(), drStr.get());
                redisTemplate.opsForHash().putIfAbsent(redisDbKeys.getDomainRuleHash(), dr.getSiteName(), drStr.get());
            }
        }
    }

    /**
     * desc:从redis中读取配置
     **/
    public Map<String, String> readForHashMap(String dbKey) {
        Map<String, String> configs = redisTemplate.opsForHash().entries(dbKey);
        return configs;
    }

    public List<String> readForList(String dbKey) {
        List<String> configs = redisTemplate.opsForList().range(dbKey, 0, -1);
        return configs;
    }
}

