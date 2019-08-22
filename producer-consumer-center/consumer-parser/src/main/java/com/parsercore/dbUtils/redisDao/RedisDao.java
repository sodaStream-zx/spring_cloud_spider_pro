package com.parsercore.dbUtils.redisDao;

import com.parsercore.dbUtils.mysqlDao.IMysqlDao;
import commoncore.customUtils.SerializeUtil;
import commoncore.entity.loadEntity.DomainRule;
import commoncore.entity.loadEntity.MyNew;
import commoncore.entity.loadEntity.RedisDbKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author 一杯咖啡
 * @desc 从redis中获取数据页面  兼任 数据提取工具
 * @createTime 2018-12-21-15:36
 */
@Component
public class RedisDao implements IRedisDao {
    private static Logger LOG = LoggerFactory.getLogger(RedisDao.class);
    private IMysqlDao<MyNew, DomainRule> iMysqlDao;
    private RedisTemplate redisTemplate;
    private RedisDbKeys redisDbKeys;

    @Autowired
    public RedisDao(IMysqlDao<MyNew, DomainRule> iMysqlDao, RedisTemplate redisTemplate,
                    RedisDbKeys redisDbKeys) {
        this.iMysqlDao = iMysqlDao;
        this.redisTemplate = redisTemplate;
        this.redisDbKeys = redisDbKeys;
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
            redisTemplate.opsForHash().put(redisDbKeys.getDomainRuleHash(), domainRule.getSiteName(), domainRuleStr.get());
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
        String drStr = (String) redisTemplate.opsForHash().get(redisDbKeys.getDomainRuleHash(), siteName);
        /*if (null == drStr || "".equals(drStr)) {
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
        } else {*/
        Optional<DomainRule> dObj = SerializeUtil.deserializeToObject(drStr);
        return dObj.isPresent() ? dObj.get() : null;
//        }
    }
}
