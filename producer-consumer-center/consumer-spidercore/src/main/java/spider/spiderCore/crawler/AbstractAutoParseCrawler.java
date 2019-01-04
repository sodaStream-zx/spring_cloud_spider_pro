package spider.spiderCore.crawler;

import commoncore.entity.httpEntity.ResponsePage;
import commoncore.entity.requestEntity.CrawlDatum;
import commoncore.entity.requestEntity.CrawlDatums;
import commoncore.entity.requestEntity.Links;
import commoncore.entity.requestEntity.entityTools.RegexRule;
import org.jsoup.nodes.Document;
import spider.spiderCore.fetcher.IFetcherTools.DefaultContentPageFilter;
import spider.spiderCore.fetcher.IFetcherTools.Executor;
import spider.spiderCore.http.ISendRequest;
import spider.spiderCore.spiderConfig.configUtil.ConfigurationUtils;

/**
 * 自动爬取解析爬虫
 */
public abstract class AbstractAutoParseCrawler extends Crawler implements Executor, DefaultContentPageFilter {

    /**
     * 是否自动抽取符合正则的链接并加入后续任务
     */
    protected boolean autoParse = true;
    protected DefaultContentPageFilter defaultContentPageFilter;
    protected ISendRequest<ResponsePage> iSendRequest;

    public AbstractAutoParseCrawler() {
        this.executor = this;
        this.defaultContentPageFilter = this;
    }

    @Override
    protected void registerOtherConfigurations() {
        super.registerOtherConfigurations();
        //当前对象配置，付给当前对象配置
        ConfigurationUtils.setTo(this, defaultContentPageFilter);
    }

    /**
     * URL正则约束
     */
    protected RegexRule regexRule = new RegexRule();

    /**
     * 页面解析器（页面urls提取）
     *
     * @param datum 任务对象
     * @param next  当前任务对象提取出来的接下来的任务
     */
    @Override
    public void execute(CrawlDatum datum, CrawlDatums next) {
        ResponsePage responsePage = iSendRequest.converterResponsePage(datum);
        //抽取当前页面符合条件的urls
        if (autoParse && !regexRule.isEmpty()) {
            parseLink(responsePage, next);
        }
        //传输数据给解析器
        defaultContentPageFilter.getContentPageData(responsePage);
        afterParse(responsePage, next);
    }

    protected void afterParse(ResponsePage responsePage, CrawlDatums next) {
        //单个页面正文抽取，任务urls抽取完成之后，执行该函数
    }

    /**
     * @Desc: [获取网页中符合 规则的url]
     */
    protected void parseLink(ResponsePage responsePage, CrawlDatums next) {
        String contentType = responsePage.contentType();
        if (contentType != null && contentType.contains("text/html")) {
            Document doc = responsePage.doc();
            if (doc != null) {
                //从页面中取出需要的url 形成接下来的任务
                Links links = new Links().addByRegex(doc, regexRule, getConfig().getAutoDetectImg());
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
