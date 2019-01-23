package parsercore.fetcherCore.generatorcore;

import commoncore.customUtils.SerializeUtil;
import commoncore.entity.httpEntity.ParseData;
import commoncore.entity.loadEntity.RedisDbKeys;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-15-15:17
 */
@Component
public class ParseDataGenerator implements IParseDataGenerator<ParseData> {
    private static final Logger log = Logger.getLogger(ParseDataGenerator.class);
    private IResponseDataFilter<ParseData> iResponseDataFilter;
    private RedisDbKeys redisDbKeys;
    private RedisTemplate redisTemplate;

    @Autowired
    public ParseDataGenerator(IResponseDataFilter<ParseData> iResponseDataFilter,
                              RedisTemplate redisTemplate,
                              RedisDbKeys redisDbKeys) {
        this.iResponseDataFilter = iResponseDataFilter;
        this.redisTemplate = redisTemplate;
        this.redisDbKeys = redisDbKeys;
    }

    @Override
    public ParseData getData() {
        String responseDataStr = (String) redisTemplate.opsForList().leftPop(redisDbKeys.getParseList());
        if (!StringUtils.isBlank(responseDataStr)) {
            log.debug("从数据库提取数据成功.......");
            Optional<ParseData> data = SerializeUtil.deserializeToObject(responseDataStr);
            if (iResponseDataFilter != null && data.isPresent()) {
                boolean passOrNot = iResponseDataFilter.pass(data.get());
                if (passOrNot) {
                    log.info("TIP:这里可能有问题");
                    this.getData();
                } else {
                    return data.get();
                }
            }
        } else {
            log.error("redis中无后续任务");
        }

        return null;
    }
}
