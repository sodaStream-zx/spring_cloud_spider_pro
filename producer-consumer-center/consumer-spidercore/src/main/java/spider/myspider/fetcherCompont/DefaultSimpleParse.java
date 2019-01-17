package spider.myspider.fetcherCompont;

import commoncore.entity.httpEntity.ResponsePage;
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
public class DefaultSimpleParse implements ISimpleParse<CrawlDatums, ResponsePage> {
    private static final Logger log = Logger.getLogger(DefaultSimpleParse.class);
    @Autowired
    private RegexRule regexRule;

    @Override
    public CrawlDatums parseLinks(ResponsePage responsePage) {
        CrawlDatums next = new CrawlDatums();
        String contentType = responsePage.getContentType();
        if (contentType != null && contentType.contains("text/html")) {
            Document doc = responsePage.getDoc();
            if (doc != null) {
                //从页面中取出需要的url 形成接下来的任务
                Links links = new Links().addByRegex(doc, regexRule, false);
                next.add(links);
            }
        }
        return next;
    }
}
