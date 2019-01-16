package parsercore.paresRuleCore;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import parsercore.dbUtils.mysqlDao.IMysqlDao;
import parsercore.dbUtils.redisDao.IRedisDao;
import parsercore.paresEntity.DomainRule;
import parsercore.paresEntity.MyNew;
import parsercore.paresRuleCore.core.IRuleFactory;

import java.util.HashMap;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-25-13:07
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RuleFactoryFactory implements IRuleFactory<DomainRule> {
    private static final Logger log = Logger.getLogger(RuleFactoryFactory.class);
    private final HashMap<String, DomainRule> ruleList = new HashMap<>(2);
    @Autowired
    private IRedisDao iRedisDao;
    @Autowired
    private IMysqlDao<MyNew, DomainRule> mysqlDao;
    @Value(value = "${my.ruleFactory.maxSize}")
    private int ruleListMaxSize;

    @Override
    public DomainRule getRule(String siteName) {
        DomainRule domainRule = null;
        if (!StringUtils.isBlank(siteName)) {
            domainRule = ruleList.get(siteName);
            if (domainRule == null) {
                domainRule = iRedisDao.getDomainRuleFromRedis(siteName);
                cacheToMemory(siteName, domainRule);
                if (domainRule == null) {
                    domainRule = mysqlDao.getDomainRuleFromMysql(siteName);
                    cacheToMemory(siteName, domainRule);
                    log.warn("redis中没有该规则，从mysql中获取，并缓存到redis和内存");
                } else {
                    log.warn("从redis中获取规则");
                }
            } else {
                log.info("从内存中获取规则规则");
            }
        }
        return domainRule;
    }

    /**
     * desc: 缓存配置到内存中
     **/
    public void cacheToMemory(String siteName, DomainRule domainRule) {
        if (!(ruleList.size() > ruleListMaxSize)) {
            ruleList.clear();
        }
        this.ruleList.put(siteName, domainRule);
    }
}
