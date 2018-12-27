package parsercore.fetchercore.coreimp;

import commoncore.entity.responseEntity.ResponseData;
import org.springframework.stereotype.Component;
import parsercore.fetchercore.generatorcore.IDataFilter;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-26-15:15
 */
@Component
public class RedisDataFilter implements IDataFilter<ResponseData> {
    @Override
    public boolean pass(ResponseData data) {
        //do something
        return false;
    }
}
