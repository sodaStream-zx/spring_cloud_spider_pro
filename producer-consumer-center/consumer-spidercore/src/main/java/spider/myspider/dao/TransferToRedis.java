package spider.myspider.dao;

import commoncore.customUtils.SerializeUtil;
import commoncore.entity.httpEntity.ParseData;
import commoncore.entity.httpEntity.ResponseData;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import spider.spiderCore.iexecutorCom.TransferToParser;

import java.util.Optional;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-27-15:57
 */
@Component
public class TransferToRedis implements TransferToParser<ResponseData> {
    private static final Logger log = Logger.getLogger(TransferToRedis.class);
    private RedisTemplate redisTemplate;

    @Autowired
    public TransferToRedis(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void transfer(ResponseData pd) {
        log.debug("传输数据到解析模块" + pd.getHtml().substring(0, 10));
        ParseData data = new ParseData(pd.getSiteName(), pd.getFetcherTask().getUrl(), pd.getContentType(), pd.getHtml());
        Optional<String> rd = SerializeUtil.serializeToString(data);
        if (rd.isPresent()) {
            redisTemplate.opsForList().leftPush("responseList", rd.get());
        }
    }
}
