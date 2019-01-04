package spider.spiderCore.crawldb;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2019-01-04-12:15
 */
public interface IGenerator<T> {
    T next();

    T nextWithoutFilter();

    int totalGeneretNum();

    void clear();

    void close();
}
