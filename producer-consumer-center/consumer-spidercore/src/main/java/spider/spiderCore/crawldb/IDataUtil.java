package spider.spiderCore.crawldb;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2019-01-04-14:05
 */
public interface IDataUtil {
    IDbWritor getIDbWritor();

    IDbManager getIDbManager();

    IGenerator getIGenerator();

}
