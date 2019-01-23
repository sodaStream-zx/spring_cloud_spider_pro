package spider.myspider.executorCompont;

import commoncore.entity.httpEntity.ResponseData;
import commoncore.entity.loadEntity.WebSiteConf;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import spider.spiderCore.entitys.RegexRule;
import spider.spiderCore.iexecutorCom.IContentNeeded;
import spider.spiderCore.iexecutorCom.TransferToParser;

/**
 * @author Twilight
 * @desc 筛选当前页面时候需要 传送到正文解析器 解析
 * @createTime 2019-01-07-22:38
 */
@Component(value = "defaultContentPageFilter")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultContentNeeded implements IContentNeeded {
    private static final Logger log = Logger.getLogger(DefaultContentNeeded.class);
    private RegexRule regexRule;
    private TransferToParser<ResponseData> transferToParser;
    private WebSiteConf webSiteConf;

    @Autowired
    public DefaultContentNeeded(RegexRule regexRule,
                                TransferToParser<ResponseData> transferToParser,
                                WebSiteConf webSiteConf) {
        this.regexRule = regexRule;
        this.transferToParser = transferToParser;
        this.webSiteConf = webSiteConf;
    }

    /**
     * desc: 符合正文提取规则。调用自定义解析页面
     **/
    @Override
    public void getContentPageData(ResponseData responseData) {
        //匹配正文筛选规则 url
        if (regexRule.satisfyContentRegex(responseData.getFetcherTask().getUrl())) {
            log.info("添加responsePage 的 属性 网站名 作为解析模块的识别");
            responseData.setSiteName(webSiteConf.getSiteName());
            log.debug("符合要求。传输到解析模块" + responseData.getSiteName());
            transferToParser.transfer(responseData);
        }
    }
}
