package spider.myspider.redisComponent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spider.spiderCore.idbcore.IDataBase;

/**
 * @desc redis数据库表
 * @author 一杯咖啡
 */
@Component
public class RedisDb implements IDataBase<String> {

    @Value(value = "${redisDb.seedsList}")
    private String seedsList;
    @Value(value = "${redisDb.undoneList}")
    private String undoneList;
    @Value(value = "${redisDb.lindList}")
    private String lindList;
    @Value(value = "${redisDb.redirectList}")
    private String redirectList;

    @Override
    public String getCrawlDB() {
        return seedsList;
    }

    @Override
    public String getFetchDB() {
        return undoneList;
    }

    @Override
    public String getLinkDB() {
        return lindList;
    }

    @Override
    public String getRedirectDB() {
        return redirectList;
    }

    /**
     * desc: 清空数据库
     **/
    @Override
    public void clear() {

    }
}
