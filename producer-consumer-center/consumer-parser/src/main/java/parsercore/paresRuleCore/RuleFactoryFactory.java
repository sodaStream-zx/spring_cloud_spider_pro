package parsercore.paresRuleCore;

import commoncore.entity.loadEntity.DomainRule;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import parsercore.dbUtils.redisDao.IRedisDao;
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
    private static final Logger log = LoggerFactory.getLogger(RuleFactoryFactory.class);
    private final HashMap<String, DomainRule> ruleList = new HashMap<>(2);
    private IRedisDao iRedisDao;
    private int ruleListMaxSize;

    @Autowired
    public RuleFactoryFactory(IRedisDao iRedisDao,
                              @Value(value = "${my.ruleFactory.maxSize}") int ruleListMaxSize) {
        this.iRedisDao = iRedisDao;
        this.ruleListMaxSize = ruleListMaxSize;
    }

    @Override
    public DomainRule getRule(String siteName) {
        DomainRule domainRule = null;
        if (!StringUtils.isBlank(siteName)) {
            domainRule = ruleList.get(siteName);
            if (domainRule == null) {
                domainRule = iRedisDao.getDomainRuleFromRedis(siteName);
                if (domainRule != null) {
                    cacheToMemory(siteName, domainRule);
                    log.info("redis取得解析器");
                } else {
                    log.warn("redis中没有该网站解析器");
                }
            }
            log.debug("内存中取得解析器");
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
