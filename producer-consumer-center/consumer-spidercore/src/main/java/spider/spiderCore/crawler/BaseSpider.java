package spider.spiderCore.crawler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spider.spiderCore.fetchercore.Fetcher;
import spider.spiderCore.idbcore.IDataUtil;
import spider.spiderCore.idbcore.IGenerator;


/**
 * @author Twilight
 * @desc 简易爬虫
 * @createTime 2019-01-10-15:09
 */
@Component
public class BaseSpider extends AbstractSpider {
    private static final Logger log = Logger.getLogger(BaseSpider.class);
    @Autowired
    private IGenerator iGenerator;

    @Autowired
    public BaseSpider(SeedData seedData, RegexRuleData regexRuleData, Fetcher fetcher, IDataUtil iDataUtil) {
        super(seedData, regexRuleData, fetcher, iDataUtil);
    }

    @Override
    public void afterStopSpider() {
        //清除前一次记录
        log.info("-----------------清楚最大任务上限-------------------");
        iGenerator.clear();
    }
}
