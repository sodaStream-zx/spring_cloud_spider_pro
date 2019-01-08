package parsercore.paresUtil;

import commoncore.entity.httpEntity.ResponsePage;
import commoncore.entity.paresEntity.DomainRule;
import commoncore.entity.paresEntity.MyNew;
import commoncore.parseTools.ParesCounter;
import commoncore.parseTools.Selectors;
import commoncore.parseTools.StringSplitUtil;
import commoncore.parseTools.TimeFilter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * 自定义解析器
 *
 * @author 一杯咖啡
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MyParesContent implements ParesContent {
    private static final Logger LOG = Logger.getLogger(MyParesContent.class);
    @Autowired
    private ParesCounter paresCounter;

    @Override
    public String toString() {
        return "MyParesContent{" +
                "paresCounter=" + paresCounter +
                '}';
    }

    /**
     * @Title：${enclosing_method}
     * @Description: [页面正文提取]
     */
    @Override
    public MyNew paresContent(ResponsePage page, DomainRule domainRule) {
        //有效连接数+1
        paresCounter.getTotalData().incrementAndGet();
        String title = page.getDoc().title();
        if (title.trim().equals("")) {
            title = Selectors.IdClassSelect(page, StringSplitUtil.splitRule(domainRule.getTitle_rule()));
        }
        //获取正文
        String content = Selectors.detaliSelect(page, StringSplitUtil.splitRule(domainRule.getContent_rule()));
        if (!content.trim().equals("")) {
            //正文不为空 获取 作者，媒体，时间
            String media = Selectors.detaliSelect(page, StringSplitUtil.splitRule(domainRule.getMedia_rule()));
            String author = Selectors.detaliSelect(page, StringSplitUtil.splitRule(domainRule.getAnthor_rule()));
            String time = Selectors.detaliSelect(page, StringSplitUtil.splitRule(domainRule.getTime_rule()));
            //截取指定长度字符串
            time = StringSplitUtil.SubStr(TimeFilter.getTimeByReg(time));
            media = StringSplitUtil.SubStr(media);
            author = StringSplitUtil.SubStr(author);
            String url = page.getCrawlDatum().url();
            MyNew myNew = GenerateNews(title, url, content, media, author, time);
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
    public MyNew GenerateNews(String title, String url, String content, String media, String author, String time) {
        MyNew myNew = new MyNew();
        myNew.setTitle(title);
        myNew.setURL(url);
        myNew.setContent(content);
        myNew.setMedia(media);
        myNew.setAnthor(author);
        myNew.setTime(time);
        return myNew;
    }
}
