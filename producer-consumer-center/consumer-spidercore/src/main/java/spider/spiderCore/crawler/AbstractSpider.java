package spider.spiderCore.crawler;

import commoncore.entity.configEntity.SiteConfig;
import commoncore.entity.requestEntity.CrawlDatums;
import commoncore.parseTools.StringSplitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spider.spiderCore.crawldb.IDataUtil;
import spider.spiderCore.crawldb.IDbWritor;
import spider.spiderCore.fetcher.Fetcher;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-08-14:34
 */
public class AbstractSpider implements ISpider {

    private static final Logger LOG = LoggerFactory.getLogger(Crawler.class);

    private int status;

    public final static int RUNNING = 1;
    public final static int STOPED = 2;
    //网站配置
    private SiteConfig siteConfig;
    protected int threads = 50;

    //入口urls
    protected SeedData seedData;
    //正则规则
    protected RegexRuleData regexRuleData;
    //执行调度器
    protected Fetcher fetcher;
    //数据存储器（考虑将种子注入和 解析注入分开）
    protected IDataUtil iDataUtil;

    /**
     * urlRules url 解析正则表达式
     * seeds 入口 url
     * conPickRules 正文提取正则表达式
     */
    @Override
    public void loadConfig() {
        //url 提取正则
        String[] urlRules = StringSplitUtil.splitRule(siteConfig.getUrlPares());
        //seeds 种子url
        String[] seeds = StringSplitUtil.splitRule(siteConfig.getSeeds());
        //conPickRules 需要提取正文的 url 正则表达式
        String[] conPickRules = StringSplitUtil.splitRule(siteConfig.getPageParse());

        //配置规则实例
        for (String str : seeds) {
            LOG.info("入口：" + str);
            seedData.addSeed(str, true);
        }
        for (String n : conPickRules) {
            LOG.info("正文提取规则注入:" + n);
            regexRuleData.getRegexRule().addContentRegexRule(n);
        }
        for (String u : urlRules) {
            LOG.info("url提取规则注入:" + u);
            regexRuleData.addRegex(u);
        }
    }

    /**
     * desc: 注入入口种子到数据库中
     **/
    @Override
    public void injectSeeds() {
        IDbWritor iDbWritor = iDataUtil.getIDbWritor();
        CrawlDatums seeds = seedData.getSeeds();
        CrawlDatums forceSeeds = seedData.getForcedSeeds();
        try {
            if (seeds.isEmpty()) {
                iDbWritor.injectList(seeds, false);
            }
            if (!seedData.getForcedSeeds().isEmpty()) {
                iDbWritor.injectList(forceSeeds, true);
            }
        } catch (Exception e) {
            LOG.error("添加种子到数据库失败");
            LOG.error(e.toString());
        }
    }

    @Override
    public void startSpider() {
        LOG.info(this.toString());
        //判断是否断点。不开启，则，启动前 清理数据库
        if (!siteConfig.isRes()) {
            if (iDataUtil.getIDbManager().isDBExists()) {
                iDataUtil.getIDbManager().clear();
            }
            if (seedData.getSeeds().isEmpty() && seedData.getForcedSeeds().isEmpty()) {
                LOG.error("error:Please add at least one seed");
                return;
            }
        }
        iDataUtil.getIDbManager().open();
        //注入入口
        this.injectSeeds();

        status = RUNNING;
        for (int i = 0; i < siteConfig.getDeepPath(); i++) {
            if (status == STOPED) {
                break;
            }
            LOG.info("start depth " + (i + 1));
            long startTime = System.currentTimeMillis();
            fetcher.setThreads(threads);
            int totalGenerate = fetcher.fetcherStart();

            long endTime = System.currentTimeMillis();
            long costTime = (endTime - startTime) / 1000;

            LOG.info("\ndepth " + (i + 1) + " finish: " +
                    "\n              this depth total urls: " + totalGenerate + "" +
                    "\n              this depth total time: " + costTime + " seconds");
            if (totalGenerate == 0) {
                break;
            }
        }
        iDataUtil.getIDbManager().close();
        this.afterStopSpider();
    }

    @Override
    public void stopSpider() {
        status = STOPED;
        fetcher.stopFetcher();
    }

    @Override
    public void afterStopSpider() {

    }
}
