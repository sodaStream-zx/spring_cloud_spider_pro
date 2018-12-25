package parsercore.dbUtils.gainDao;

import commoncore.entity.paresEntity.DomainRule;
import commoncore.entity.responseEntity.ResponseData;
import commoncore.parseTools.SerializeUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import parsercore.dbUtils.Istore.IMysqlDao;
import parsercore.dbUtils.gainDao.IGain.IRedisDao;

import java.util.concurrent.TimeUnit;

/**
 * @author 一杯咖啡
 * @desc 从redis中获取数据页面
 * @createTime 2018-12-21-15:36
 */
@Component
public class RedisDao implements IRedisDao {
    private static Logger LOG = Logger.getLogger(RedisDao.class);
    @Autowired
    private IMysqlDao iMysqlDao;
    @Autowired
    private RedisTemplate redisTemplate;

    SerializeUtil serializeUtil = new SerializeUtil();

    @Override
    public ResponseData getResponseDataFromRedis(String responseList) {
        ResponseData responseData = null;
        String responseDataStr = (String) redisTemplate.opsForList().leftPop(responseList);
        if (!checkNull(responseDataStr)) {
            try {
                responseData = (ResponseData) serializeUtil.deserializeToObject(responseDataStr);
            } catch (Exception e) {
                LOG.error("反序列化异常" + e.getMessage());
            }
        }else {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("redis没有数据");
            } catch (InterruptedException e) {
                LOG.error("休眠异常");
            }
            this.getResponseDataFromRedis(responseList);
        }
        return responseData;
    }

    /**
     * desc:缓存到reids中
     **/
    @Override
    public void addDomainRuleToRedis(DomainRule domainRule) {
        String domainRuleStr = "";
        try {
            domainRuleStr = serializeUtil.serializeToString(domainRule);
        } catch (Exception e) {
            LOG.error("序列化异常");
        }
        redisTemplate.opsForList().rightPush(domainRule.getSiteName(), domainRuleStr);
    }

    /**
     * desc:获取reids中
     **/
    @Override
    public DomainRule getDomainRuleFromRedis(String siteName) {
        DomainRule domainRule = null;
        String drStr = (String) redisTemplate.opsForList().leftPop(siteName);
        if (null == drStr || "".equals(drStr)) {
            LOG.info("redis中没有值");
            domainRule = iMysqlDao.getDomainRuleFromMysql(siteName);
            this.addDomainRuleToRedis(domainRule);
            return domainRule;
        } else {
            try {
                domainRule = (DomainRule) serializeUtil.deserializeToObject(drStr);
            } catch (Exception e) {
                LOG.error("反序列化异常");
            }
            return domainRule;
        }
    }

    /**
     * desc:字符串空值检测
     **/
    public boolean checkNull(String str) {
        if (null == str || str.equals("")) {
            LOG.error("str = " + str + " is NULL or nothing ");
            return true;
        } else {
            return false;
        }
    }
}
