package parsercore.paresUtil;

import commoncore.entity.paresEntity.DomainRule;
import commoncore.entity.paresEntity.MyNew;
import commoncore.entity.responseEntity.ResponsePage;
import commoncore.parseTools.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 自定义解析器
 * @author 一杯咖啡
 */
@Component
//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MyParesContent implements ParesContent {
    private static final Logger LOG = Logger.getLogger(ParesUtil.class);
    private ParesUtil paresUtil;
    private ParesCounter paresCounter;
    private RulesSplitUtil rulesSplitUtil;
    private Selectors selectors;
    private TimeFilter timeFilter;

    @Autowired
    public void MyParesContent(ParesUtil paresUtil) {
        this.paresUtil = paresUtil;
        paresCounter = paresUtil.getParesCounter();
        rulesSplitUtil = paresUtil.getRulesSplitUtil();
        selectors = paresUtil.getSelectors();
        timeFilter = paresUtil.getTimeFilter();
    }

    @Override
    public String toString() {
        return "MyParesContent{" +
                "paresUtil=" + paresUtil.toString() +
                '}';
    }

    /**
     * @Title：${enclosing_method}
     * @Description: [页面正文提取]
     */
    @Override
    public MyNew paresContent(ResponsePage page,DomainRule domainRule) {
        //有效连接数+1
        paresCounter.getTotalData().incrementAndGet();
        String title = page.doc().title();
        if (title.trim().equals("")) {
            title = selectors.IdClassSelect(page, rulesSplitUtil.splitRule(domainRule.getTitle_rule()));
        }
        //获取正文
        String content = selectors.detaliSelect(page, rulesSplitUtil.splitRule(domainRule.getContent_rule()));
        if (!content.trim().equals("")) {
            //正文不为空 获取 作者，媒体，时间
            String media = selectors.detaliSelect(page, rulesSplitUtil.splitRule(domainRule.getMedia_rule()));
            String author = selectors.detaliSelect(page, rulesSplitUtil.splitRule(domainRule.getAnthor_rule()));
            String time = selectors.detaliSelect(page, rulesSplitUtil.splitRule(domainRule.getTime_rule()));
            //截取指定长度字符串
            time = rulesSplitUtil.SubStr(timeFilter.getTimeByReg(time));
            media = rulesSplitUtil.SubStr(media);
            author = rulesSplitUtil.SubStr(author);
            String url = page.url();
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
