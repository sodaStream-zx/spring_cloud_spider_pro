package spider.spiderCore.crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spider.spiderCore.fetchercore.Fetcher;
import spider.spiderCore.idbcore.IDataUtil;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-10-15:09
 */
@Component
public class BaseSpider extends AbstractSpider {
    @Autowired
    public BaseSpider(SeedData seedData, RegexRuleData regexRuleData, Fetcher fetcher, IDataUtil iDataUtil) {
        super(seedData, regexRuleData, fetcher, iDataUtil);
    }

    @Override
    public void afterStopSpider() {

    }
}
