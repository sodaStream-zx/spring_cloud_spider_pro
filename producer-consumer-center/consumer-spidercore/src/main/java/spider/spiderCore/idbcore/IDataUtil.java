package spider.spiderCore.idbcore;

/**
 * @author 一杯咖啡
 * @desc 数据库工具集合
 * @createTime 2019-01-04-14:05
 */
public interface IDataUtil {
    IDbWritor getIDbWritor();

    IDbManager getIDbManager();

    IGenerator getIGenerator();

}
