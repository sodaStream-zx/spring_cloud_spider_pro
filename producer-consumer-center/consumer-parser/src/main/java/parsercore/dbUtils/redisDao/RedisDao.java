package parsercore.dbUtils.redisDao;

import commoncore.customUtils.SerializeUtil;
import commoncore.entity.loadEntity.DomainRule;
import commoncore.entity.loadEntity.MyNew;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import parsercore.dbUtils.mysqlDao.IMysqlDao;

import java.util.Optional;

/**
 * @author 一杯咖啡
 * @desc 从redis中获取数据页面  兼任 数据提取工具
 * @createTime 2018-12-21-15:36
 */
@Component
public class RedisDao implements IRedisDao {
    private static Logger LOG = Logger.getLogger(RedisDao.class);
    private IMysqlDao<MyNew, DomainRule> iMysqlDao;
    private RedisTemplate redisTemplate;
    private String domainMapName;

    @Autowired
    public RedisDao(IMysqlDao<MyNew, DomainRule> iMysqlDao, RedisTemplate redisTemplate,
                    @Value(value = "${my.domain.mapName}") String domainMapName) {
        this.iMysqlDao = iMysqlDao;
        this.redisTemplate = redisTemplate;
        this.domainMapName = domainMapName;
    }

    /**
     * desc:缓存到reids中
     **/
    @Override
    public void addDomainRuleToRedis(DomainRule domainRule) {
        String key = domainRule.getSiteName();
        Optional<String> domainRuleStr = SerializeUtil.serializeToString(domainRule);
        if (domainRuleStr.isPresent()) {
            LOG.info("存入规则到redis中" + key);
            redisTemplate.opsForHash().put(domainMapName, domainRule.getSiteName(), domainRuleStr.get());
            //redisTemplate.opsForList().rightPush(key, domainRuleStr);
        } else {
            LOG.error("未序列化出值");
        }
    }

    /**
     * desc:获取reids中 解析器规则
     **/
    @Override
    public DomainRule getDomainRuleFromRedis(String siteName) {
        String drStr = (String) redisTemplate.opsForHash().get(domainMapName, siteName);
        if (null == drStr || "".equals(drStr)) {
            DomainRule domainRule;
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
            Optional<DomainRule> dObj = SerializeUtil.deserializeToObject(drStr);
            return dObj.isPresent() ? dObj.get() : null;
        }
    }
}
