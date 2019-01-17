package spider.myspider.dao;

import commoncore.customUtils.SerializeUtil;
import commoncore.entity.httpEntity.ParseData;
import commoncore.entity.httpEntity.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import spider.spiderCore.iexecutorCom.TransferToParser;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-27-15:57
 */
@Component
public class TransferToRedis implements TransferToParser<ResponsePage> {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void transfer(ResponsePage pd) {
        ParseData data;
        //data = new ResponseData(responsePage.getSiteName(), responsePage.getCrawlDatum(), responsePage.getCode(), responsePage.getContentType(), responsePage.getContent());
        data = new ParseData(pd.getSiteName(), pd.getCrawlDatum().getUrl(), pd.getContentType(), pd.getHtml());
        String rd = null;
        try {
            rd = SerializeUtil.serializeToString(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        redisTemplate.opsForList().rightPush("responseList", rd);
        //LOG.warn("tip:将page 存入redis中，供解析模块提取数据");
    }
}
