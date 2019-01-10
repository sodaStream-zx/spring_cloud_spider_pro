package spider.myspider.redisComponent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spider.spiderCore.idbcore.IDataUtil;
import spider.spiderCore.idbcore.IDbManager;
import spider.spiderCore.idbcore.IDbWritor;
import spider.spiderCore.idbcore.IGenerator;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2019-01-04-13:13
 */
@Component
public class DefaultRedisDataBase implements IDataUtil {
    @Autowired
    private IGenerator iGenerator;
    @Autowired
    private IDbWritor iDbWritor;
    @Autowired
    private IDbManager iDbManager;

    @Override
    public String toString() {
        return "DefaultRedisDataBase{" +
                "iGenerator=" + iGenerator +
                ", iDbWritor=" + iDbWritor +
                ", iDbManager=" + iDbManager +
                '}';
    }

    @Override
    public IDbWritor getIDbWritor() {
        return iDbWritor;
    }

    @Override
    public IDbManager getIDbManager() {
        return iDbManager;
    }

    @Override
    public IGenerator getIGenerator() {
        return iGenerator;
    }
}
