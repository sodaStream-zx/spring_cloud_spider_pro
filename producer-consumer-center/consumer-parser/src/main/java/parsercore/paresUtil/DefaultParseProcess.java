package parsercore.paresUtil;

import commoncore.customUtils.ParesCounter;
import commoncore.customUtils.Selectors;
import commoncore.customUtils.StringSplitUtil;
import commoncore.customUtils.TimeFilter;
import commoncore.entity.httpEntity.ParseData;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import parsercore.paresEntity.DomainRule;
import parsercore.paresEntity.MyNew;


/**
 * 自定义解析器
 *
 * @author 一杯咖啡
 */
@Component(value = "parseContent")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultParseProcess implements IParseProcess<MyNew, ParseData, DomainRule> {
    private static final Logger LOG = Logger.getLogger(DefaultParseProcess.class);
    @Autowired
    private ParesCounter paresCounter;

    /**
     * @Title：${enclosing_method}
     * @Description: [页面正文提取]
     */
    @Override
    public MyNew paresContent(ParseData data, DomainRule domainRule) {
        Document doc = Jsoup.parse(data.getContentString());
        //有效连接数+1
        paresCounter.getTotalData().incrementAndGet();
        String title = doc.title();
        if (title.trim().equals("")) {
            title = Selectors.IdClassSelect(doc, StringSplitUtil.splitRule(domainRule.getTitle_rule()));
        }
        //获取正文
        String content = Selectors.detaliSelect(doc, StringSplitUtil.splitRule(domainRule.getContent_rule()));
        if (!content.trim().equals("")) {
            //正文不为空 获取 作者，媒体，时间
            String media = Selectors.detaliSelect(doc, StringSplitUtil.splitRule(domainRule.getMedia_rule()));
            String author = Selectors.detaliSelect(doc, StringSplitUtil.splitRule(domainRule.getAnthor_rule()));
            String time = Selectors.detaliSelect(doc, StringSplitUtil.splitRule(domainRule.getTime_rule()));
            //截取指定长度字符串
            time = StringSplitUtil.SubStr(TimeFilter.getTimeByReg(time));
            media = StringSplitUtil.SubStr(media);
            author = StringSplitUtil.SubStr(author);
            String url = data.getPageUrl();
            MyNew myNew = generateNews(title, url, content, media, author, time);
            paresCounter.getValid().incrementAndGet();
            return myNew;
        } else {
            //正文为空
            paresCounter.getInvalid().incrementAndGet();
            LOG.warn("已过滤正文为空的新闻");
            return null;
        }
    }

    /**
     * @Title：${enclosing_method}
     * @Description: [生成MyNew对象]
     */
    public MyNew generateNews(String title, String url, String content, String media, String author, String time) {
        MyNew myNew = new MyNew();
        myNew.setTitle(title);
        myNew.setURL(url);
        myNew.setContent(content);
        myNew.setMedia(media);
        myNew.setAuthor(author);
        myNew.setTime(time);
        return myNew;
    }
}
