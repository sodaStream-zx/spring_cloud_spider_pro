package loadercore.dao;

import commoncore.customUtils.SerializeUtil;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 * <p>项目名称: ${小型分布式爬虫} </p>
 * <p>描述: [redis 数据操作 ] </p>
 * <p>创建时间: ${date} </p>
 *
 * @author <a href="mail to: 1139835238@qq.com" rel="nofollow">whitenoise</a>
 **/
@Component
public class RedisDao {
    private static final Logger log = Logger.getLogger(RedisDao.class);
    private RedisTemplate redisTemplate;

    public RedisDao(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * @Title：${enclosing_method}
     * @Description: [redis 插入序列化的对象]
     */
    public boolean insertAObject(String keyString, Object obj) throws Exception {
        String objStr = null;
        objStr = SerializeUtil.serializeToString(obj);
        Long reindex = redisTemplate.opsForSet().add(keyString, objStr);
        return reindex != 0;
    }
}
