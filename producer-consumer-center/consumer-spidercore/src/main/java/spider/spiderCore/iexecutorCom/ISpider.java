package spider.spiderCore.iexecutorCom;

import commoncore.entity.loadEntity.SiteConfig;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-08-14:55
 */
public interface ISpider {
    boolean loadConfig();

    boolean injectSeeds();

    boolean spiderProcess();

    boolean stopSpider();

    void afterStopSpider();

    void setConfig(SiteConfig config);
}
