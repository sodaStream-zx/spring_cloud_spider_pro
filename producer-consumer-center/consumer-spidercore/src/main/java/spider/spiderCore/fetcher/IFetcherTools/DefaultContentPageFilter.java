package spider.spiderCore.fetcher.IFetcherTools;

import commoncore.entity.configEntity.SiteConfig;
import commoncore.entity.httpEntity.ResponsePage;
import commoncore.entity.requestEntity.entityTools.RegexRule;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Twilight
 * @desc 后续任务过滤后发送到
 * @createTime 2019-01-07-22:38
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultContentPageFilter implements IContentPageFilter {
    private static final Logger log = Logger.getLogger(DefaultContentPageFilter.class);
    @Autowired
    RegexRule regexRule;
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
        if (regexRule.satisfyContentRules(responsePage.getCrawlDatum().url())) {
            log.info("需要添加responsePage 的 属性 网站名 作为解析模块的识别");
            responsePage.setSiteName(siteConfig.getSiteName());
            transferToParser.transfer(responsePage);
        }
    }
}
