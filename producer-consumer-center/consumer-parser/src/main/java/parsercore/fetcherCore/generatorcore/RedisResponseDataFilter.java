package parsercore.fetcherCore.generatorcore;

import commoncore.entity.httpEntity.ResponseData;
import org.springframework.stereotype.Component;

/**
 * @author 一杯咖啡
 * @desc 数据过滤
 * @createTime 2018-12-26-15:15
 */
@Component
public class RedisResponseDataFilter implements IResponseDataFilter<ResponseData> {
    @Override
    public boolean pass(ResponseData data) {
        //do something
        return false;
    }
}
