package parsercore.dbUtils.redisDao;

import commoncore.customUtils.SerializeUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import parsercore.dbUtils.mysqlDao.IMysqlDao;
import parsercore.paresEntity.DomainRule;
import parsercore.paresEntity.MyNew;

/**
 * @author 一杯咖啡
 * @desc 从redis中获取数据页面  兼任 数据提取工具
 * @createTime 2018-12-21-15:36
 */
@Component
public class RedisDao implements IRedisDao {
    private static Logger LOG = Logger.getLogger(RedisDao.class);
    @Autowired
    private IMysqlDao<MyNew, DomainRule> iMysqlDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Value(value = "${my.domain.mapName}")
    private String domainMapName;

    /**
     * desc:缓存到reids中
     **/
    @Override
    public void addDomainRuleToRedis(DomainRule domainRule) {
        String domainRuleStr = "";
        String key = domainRule.getSiteName();
        try {
            domainRuleStr = SerializeUtil.serializeToString(domainRule);
        } catch (Exception e) {
            LOG.error("序列化异常");
        }
        LOG.info("存入规则到redis中" + key);
        redisTemplate.opsForHash().put(domainMapName, domainRule.getSiteName(), domainRuleStr);
        //redisTemplate.opsForList().rightPush(key, domainRuleStr);
    }

    /**
     * desc:获取reids中 解析器规则
     **/
    @Override
    public DomainRule getDomainRuleFromRedis(String siteName) {
        DomainRule domainRule = null;
        String drStr = (String) redisTemplate.opsForHash().get(domainMapName, siteName);
        if (null == drStr || "".equals(drStr)) {
            LOG.info("redis中没有值");
            domainRule = iMysqlDao.getDomainRuleFromMysql(siteName);
            if (domainRule == null) {
                LOG.info("mysql 没有该网站解析器配置");
                return null;
            } else {
                LOG.info("获取mysql中的数据 并缓存到redis");
                this.addDomainRuleToRedis(domainRule);
                return domainRule;
            }
        } else {
            try {
                domainRule = (DomainRule) SerializeUtil.deserializeToObject(drStr);
            } catch (Exception e) {
                LOG.error("反序列化异常");
            }
            return domainRule;
        }
    }
}
