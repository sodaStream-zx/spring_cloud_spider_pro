package spider.spiderCore.crawldb;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2019-01-04-13:05
 */
public interface IDbManager {
    boolean isDBExists();

    void clear();

    void open();

    void close();

    void merge();
}
