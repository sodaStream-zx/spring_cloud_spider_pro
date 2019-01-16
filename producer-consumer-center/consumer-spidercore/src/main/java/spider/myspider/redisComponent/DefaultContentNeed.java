package spider.myspider.redisComponent;

import commoncore.entity.configEntity.SiteConfig;
import commoncore.entity.httpEntity.ResponsePage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import spider.spiderCore.crawler.RegexRuleData;
import spider.spiderCore.iexecutorCom.IContentNeed;
import spider.spiderCore.iexecutorCom.TransferToParser;

/**
 * @author Twilight
 * @desc 筛选当前页面时候需要 传送到正文解析器 解析
 * @createTime 2019-01-07-22:38
 */
@Component(value = "defaultContentPageFilter")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultContentNeed implements IContentNeed {
    private static final Logger log = Logger.getLogger(DefaultContentNeed.class);
    @Autowired
    RegexRuleData regexRuleData;
    @Autowired
    SiteConfig siteConfig;
    @Autowired
    TransferToParser<ResponsePage> transferToParser;

    /**
     * desc: 符合正文提取规则。调用自定义解析页面
     **/
    @Override
    public void getContentPageData(ResponsePage responsePage) {
        //匹配正文筛选规则 url
        if (regexRuleData.getRegexRule().satisfyContentRegex(responsePage.getCrawlDatum().getUrl())) {
            log.debug("添加responsePage 的 属性 网站名 作为解析模块的识别");
            log.info("符合要求。传输到解析模块" + siteConfig.getSiteName());
            responsePage.setSiteName(siteConfig.getSiteName());
            transferToParser.transfer(responsePage);
        }
    }
}
