package spider.myspider.fetcherCompont;

import commoncore.entity.httpEntity.ResponsePage;
import commoncore.entity.requestEntity.CrawlDatums;
import commoncore.entity.requestEntity.Links;
import commoncore.entity.requestEntity.entityTools.RegexRule;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spider.spiderCore.crawler.ISimpleParse;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-07-15:34
 */
@Component
public class DefaultSimpleParse implements ISimpleParse {
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
