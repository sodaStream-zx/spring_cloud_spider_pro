package parsercore.dbUtils.gainDao;

import commoncore.customUtils.SerializeUtil;
import commoncore.entity.httpEntity.ResponseData;
import commoncore.entity.paresEntity.DomainRule;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import parsercore.dbUtils.Istore.IMysqlDao;
import parsercore.dbUtils.gainDao.IGain.IRedisDao;
import parsercore.fetchercore.generatorcore.IDataFilter;

/**
 * @author 一杯咖啡
 * @desc 从redis中获取数据页面  兼任 数据提取工具
 * @createTime 2018-12-21-15:36
 */
@Component
public class RedisDao implements IRedisDao {
    private static Logger LOG = Logger.getLogger(RedisDao.class);
    @Autowired
    private IMysqlDao iMysqlDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IDataFilter iDataFilter;

    @Value(value = "${my.domain.mapName}")
    private String domainMapName;
    @Value(value = "${my.responseList.redisKey}")
    private String dataKey;

    /**
     * desc: 从redis 获取待解析数据
     **/
    @Override
    public ResponseData getResponseDataFromRedis(String responseList) {
        LOG.debug("从数据库提取数据.......");
        ResponseData responseData = null;
        String responseDataStr = (String) redisTemplate.opsForList().leftPop(responseList);

        if (!StringUtils.isBlank(responseDataStr)) {
            try {
                responseData = (ResponseData) SerializeUtil.deserializeToObject(responseDataStr);
            } catch (Exception e) {
                LOG.error("反序列化异常" + e.getMessage());
            }
        } else {
            LOG.warn("redis 中无后续数据");
        }
        /*else {
             try {
                TimeUnit.SECONDS.sleep(1);
                LOG.warn("redis没有数据");
            } catch (InterruptedException e) {
                LOG.error("休眠异常");
            }
            this.getResponseDataFromRedis(responseList);
        }*/
        // responseData = new ResponseData("sadasd", new CrawlDatum("123124"), 33, "text/html", null);
        return responseData;
    }

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

    /**
     * desc:
     * redis 数据提取工具方法
     * 获取redis 中的待解析数据
     **/
    @Override
    public ResponseData getData() {
        ResponseData responseData = this.getResponseDataFromRedis(dataKey);
        if (iDataFilter != null) {
            boolean passOrNot = iDataFilter.pass(responseData);
            if (passOrNot) {
                LOG.info("TIP:这里可能有问题");
                this.getData();
            }
        }
        return responseData;
    }
}
