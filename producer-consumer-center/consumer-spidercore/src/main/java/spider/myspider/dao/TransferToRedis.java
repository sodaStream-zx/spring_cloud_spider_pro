package spider.myspider.dao;

import commoncore.entity.httpEntity.ResponseData;
import commoncore.entity.httpEntity.ResponsePage;
import commoncore.parseTools.SerializeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import spider.spiderCore.fetcher.IFetcherTools.TransferToParser;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-27-15:57
 */
@Component
public class TransferToRedis implements TransferToParser<ResponsePage> {

    @Autowired private RedisTemplate redisTemplate;
    @Override
    public void transfer(ResponsePage responsePage) {
        ResponseData responseData;
        responseData = new ResponseData(responsePage.getSiteName(), responsePage.crawlDatum(), responsePage.code(), responsePage.contentType(), responsePage.content());
        String rd = null;
        try {
            rd = new SerializeUtil().serializeToString(responseData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        redisTemplate.opsForList().rightPush("responseList", rd);
        //LOG.warn("tip:将page 存入redis中，供解析模块提取数据");
    }
}
