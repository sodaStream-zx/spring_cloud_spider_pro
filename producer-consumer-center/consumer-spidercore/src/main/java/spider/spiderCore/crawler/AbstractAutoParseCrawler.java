package spider.spiderCore.crawler;

import commoncore.entity.httpEntity.ResponsePage;
import commoncore.entity.requestEntity.CrawlDatum;
import commoncore.entity.requestEntity.CrawlDatums;
import commoncore.entity.requestEntity.Links;
import commoncore.entity.requestEntity.entityTools.RegexRule;
import org.jsoup.nodes.Document;
import spider.spiderCore.iexecutorCom.IExecutor;
import spider.spiderCore.ihttp.ISendRequest;

/**
 * 自动爬取解析爬虫
 */
public abstract class AbstractAutoParseCrawler extends Crawler {

    /**
     * 是否自动抽取符合正则的链接并加入后续任务
     */
    protected boolean autoParse = true;
    protected ISendRequest<ResponsePage> iSendRequest;
    protected IExecutor<CrawlDatums> iExecutor = null;

    public AbstractAutoParseCrawler() {
    }

    /**
     * URL正则约束
     */
    protected RegexRule regexRule;

    /**
     * 页面解析器（页面urls提取）
     *
     * @param datum 任务对象
     * @param next  当前任务对象提取出来的接下来的任务
     */
    public void execute(CrawlDatum datum, CrawlDatums next) {
        CrawlDatums crawlData = iExecutor.execute(datum);
        ResponsePage responsePage = iSendRequest.converterResponsePage(datum);
        //抽取当前页面符合条件的urls
        if (autoParse && !regexRule.isEmpty()) {
            parseLink(responsePage, next);
        }
        afterParse(responsePage, next);
    }

    protected void afterParse(ResponsePage responsePage, CrawlDatums next) {
        //单个页面正文抽取，任务urls抽取完成之后，执行该函数
    }

    /**
     * @Desc: [获取网页中符合 规则的url]
     */
    protected void parseLink(ResponsePage responsePage, CrawlDatums next) {
        String contentType = responsePage.getContentType();
        if (contentType != null && contentType.contains("text/html")) {
            Document doc = responsePage.getDoc();
            if (doc != null) {
                //从页面中取出需要的url 形成接下来的任务
                Links links = new Links().addByRegex(doc, regexRule, false);
                next.add(links);
            }
        }
    }

    /**
     * 添加URL正则约束
     *
     * @param urlRegex URL正则约束
     */
    public void addRegex(String urlRegex) {
        regexRule.addRule(urlRegex);
    }

    /**
     * 是否自动抽取符合正则的链接并加入后续任务
     *
     * @param autoParse 是否自动抽取符合正则的链接并加入后续任务
     */
    public void setAutoParse(boolean autoParse) {
        this.autoParse = autoParse;
    }

    public boolean isAutoParse() {
        return autoParse;
    }

    /**
     * 正则规则
     *
     * @param regexRule 正则规则
     */
    public void setRegexRule(RegexRule regexRule) {
        this.regexRule = regexRule;
    }

    public RegexRule getRegexRule() {
        return regexRule;
    }
}
