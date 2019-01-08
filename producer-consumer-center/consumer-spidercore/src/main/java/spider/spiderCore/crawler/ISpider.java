package spider.spiderCore.crawler;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-08-14:55
 */
public interface ISpider {
    void loadConfig();

    void injectSeeds();

    void startSpider();

    void stopSpider();

    void afterStopSpider();
}
