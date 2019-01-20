package spider.myspider.fetcherCompont;

import commoncore.entity.httpEntity.ResponseData;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import spider.spiderCore.entitys.CrawlDatums;
import spider.spiderCore.entitys.Links;
import spider.spiderCore.entitys.RegexRule;
import spider.spiderCore.iexecutorCom.ISimpleParse;

/**
 * @author Twilight
 * @desc 后续任务解析器
 * @createTime 2019-01-07-15:34
 */
@Component(value = "defaultSimpleParse")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultSimpleParse implements ISimpleParse<CrawlDatums, ResponseData> {
    private static final Logger log = Logger.getLogger(DefaultSimpleParse.class);

    private RegexRule regexRule;

    @Autowired
    public DefaultSimpleParse(RegexRule regexRule) {
        this.regexRule = regexRule;
    }

    @Override
    public CrawlDatums parseLinks(ResponseData responseData) {
        CrawlDatums next = new CrawlDatums();
        String contentType = responseData.getContentType();
        if (contentType != null && contentType.contains("text/html")) {
            Document doc = responseData.getDoc();
            if (doc != null) {
                //从页面中取出需要的url 形成接下来的任务
                CrawlDatums crawlDatums = new Links().addHrefByRegx(doc, regexRule).toCrawlDatums();
                next.add(crawlDatums);
            }
        }
        return next;
    }
}
