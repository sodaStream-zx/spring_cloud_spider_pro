package spider.myspider;


import commoncore.entity.SiteConfig;
import commoncore.entity.responseEntity.CrawlDatums;
import commoncore.entity.responseEntity.ResponseData;
import commoncore.entity.responseEntity.ResponsePage;
import commoncore.parseTools.ParesUtil;
import commoncore.parseTools.RulesSplitUtil;
import commoncore.parseTools.SerializeUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import spider.spiderCore.crawler.AbstractAutoParseCrawler;
import spider.spiderCore.http.IRequestor.Requester;

/**
 * @author 一杯咖啡
 * @desc 爬虫初始化类
 * @createTime ${YEAR}-${MONTH}-${DAY}-${TIME}
 */
@Component
public class MySpider extends AbstractAutoParseCrawler {

    private static final Logger LOG = Logger.getLogger(MySpider.class);
    /**
     * siteConfig 网站配置信息
     * paresUtil 页面解析辅助工具
     * paresContent 页面解析组件
     **/
    private SiteConfig siteconfig;
    private ParesUtil paresUtil;
    /**
     * urlRules url 解析正则表达式
     * seeds 入口 url
     * conPickRules 正文提取正则表达式
     */
    private String[] urlRules;
    private String[] seeds;
    private String[] conPickRules;

    @Autowired
    private RedisTemplate redisTemplate;

    public MySpider() {
        //设置任务上限
        //this.configuration.setTopN(600);
        //设置线程数
        this.setThreads(50);
    }

    /**
     * @param siteConfig 网站配置信息
     * @param requester  自定义请求工具 需实现requestor接口
     *                   desc :初始化爬虫组件
     */
    public void initMySpider(SiteConfig siteConfig, Requester requester, ParesUtil paresUtil) {
        this.siteconfig = siteConfig;
        this.requester = requester;
        this.paresUtil = paresUtil;

        RulesSplitUtil rulesSplitUtil = paresUtil.getRulesSplitUtil();
        urlRules = rulesSplitUtil.splitRule(siteconfig.getUrlPares());
        seeds = rulesSplitUtil.splitRule(siteconfig.getSeeds());
        conPickRules = rulesSplitUtil.splitRule(siteconfig.getPageParse());
        configSpider(siteConfig);
    }

    /**
     * @param siteConfig 网站配置信息
     *                   desc :初始化爬虫属性
     */
    public void configSpider(SiteConfig siteConfig) {
        //设置爬虫入口
        this.addMyRegx();
        //设置断点爬取
        this.setResumable(siteconfig.isRes());
        //设置自动解析url
        this.setAutoParse(siteConfig.isAutoParse());
    }

    /**
     * desc:规则注入
     */
    public void addMyRegx() {
        //注入规则
        for (String str : seeds) {
            LOG.info("入口：" + str);
            this.addSeed(str, true);
        }
        for (String n : conPickRules) {
            LOG.info("正文提取规则注入:" + n);
            this.addRegex(n);
        }
        for (String u : urlRules) {
            LOG.info("url提取规则注入:" + u);
            this.addRegex(u);
        }
    }

    /**
     * desc: 初始化完成，开始爬虫
     */
    public void startFetcher(MySpider spider) {
        try {
            spider.start(siteconfig.getDeepPath());
        } catch (Exception e) {
            LOG.error("开启爬虫失败");
        }
    }

    /**
     * desc: 符合正文提取规则。调用自定义解析页面
     **/
    @Override
    public void visit(ResponsePage responsePage, CrawlDatums next) {
        //匹配正文筛选规则 url
        ResponseData responseData;
        for (String conRegx : conPickRules) {
            if (responsePage.url().matches(conRegx)) {
                responseData = new ResponseData(siteconfig.getSiteName(), responsePage.crawlDatum(), responsePage.code(), responsePage.contentType(), responsePage.content());
                String rd = null;
                try {
                    rd = new SerializeUtil().serializeToString(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                redisTemplate.opsForList().rightPush("responseList", rd);
                //LOG.warn("tip:将page 存入redis中，供解析模块提取数据");
            }
        }
    }

    /**
     * desc: 爬虫完成后执行
     */
    @Override
    public void afterStop() {
        LOG.info("总提取量归零");
        abstractDbManager.getAbstractGenerator().setTotalGenerate(0);
        LOG.info("等待10秒 继续下一任务--------------------------");
        //System.exit(0);
    }
}
