package parsercore.fetcherCore.generatorcore;

import commoncore.entity.httpEntity.ParseData;
import org.springframework.stereotype.Component;

/**
 * @author 一杯咖啡
 * @desc 数据过滤
 * @createTime 2018-12-26-15:15
 */
@Component
public class RedisResponseDataFilter implements IResponseDataFilter<ParseData> {
    @Override
    public boolean pass(ParseData data) {
        //do something
        return false;
    }
}
