package spider.spiderCore.iexecutorCom;

import commoncore.entity.configEntity.SiteConfig;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-08-14:55
 */
public interface ISpider {
    void loadConfig();

    void injectSeeds();

    void spiderProcess();

    void stopSpider();

    void afterStopSpider();

    void setConfig(SiteConfig config);
}
