package spider.spiderCore.crawler;

import commoncore.customUtils.StringSplitUtil;
import commoncore.entity.configEntity.SiteConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spider.spiderCore.entitys.CrawlDatums;
import spider.spiderCore.entitys.RegexRule;
import spider.spiderCore.fetchercore.Fetcher;
import spider.spiderCore.idbcore.IDataUtil;
import spider.spiderCore.idbcore.IDbWritor;
import spider.spiderCore.iexecutorCom.ISpider;

import java.util.stream.Stream;

/**
 * @author Twilight
 * @desc 爬虫抽象类
 * @createTime 2019-01-08-14:34
 */
public abstract class AbstractSpider implements ISpider {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSpider.class);

    private int status;

    public final static int RUNNING = 1;
    public final static int STOPED = 2;

    //网站配置
    protected SiteConfig siteConfig;
    //入口urls
    protected SeedData seedData;
    //正则规则
    protected RegexRule regexRule;
    //执行调度器
    protected Fetcher fetcher;
    //数据存储器（考虑将种子注入和 解析注入分开）
    protected IDataUtil iDataUtil;

    public AbstractSpider(SeedData seedData, RegexRule regexRule, Fetcher fetcher, IDataUtil iDataUtil) {
        this.seedData = seedData;
        this.regexRule = regexRule;
        this.fetcher = fetcher;
        this.iDataUtil = iDataUtil;
    }

    /**
     * urlRules url 解析正则表达式
     * seeds 入口 url
     * conPickRules 正文提取正则表达式
     */
    @Override
    public boolean loadConfig() {
        if (siteConfig == null) {
            LOG.error("未加载网站配置文件");
            return false;
        }
        //seeds 种子url
        String[] seeds = StringSplitUtil.splitRule(siteConfig.getSeeds());
        if (seeds.length > 0) {
            Stream.of(seeds).forEach((x) -> seedData.addSeed(x, true));
        } else {
            LOG.error("请提供入口种子");
            return false;
        }
        //url 提取正则
        String[] urlRules = StringSplitUtil.splitRule(siteConfig.getUrlPares());
        //conPickRules 需要提取正文的 url 正则表达式
        String[] conPickRules = StringSplitUtil.splitRule(siteConfig.getPageParse());

        regexRule.addContentRegexRule(conPickRules);
        regexRule.addPickReges(urlRules);
        regexRule.addPickReges(conPickRules);
        LOG.info("url提取规则注入" + regexRule.info());
        return true;
    }

    /**
     * desc: 注入入口种子到数据库中
     **/
    @Override
    public boolean injectSeeds() {
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
            return true;
        } catch (Exception e) {
            LOG.error("添加种子到数据库失败");
            LOG.error(e.toString());
            return false;
        }
    }

    @Override
    public boolean spiderProcess() {
        boolean loadState = this.loadConfig();
        LOG.info("加载配置？" + loadState);
        //判断是否断点。不开启，则，启动前 清理数据库
        if (!siteConfig.isRes()) {
            if (iDataUtil.getIDbManager().isDBExists()) {
                iDataUtil.getIDbManager().clear();
            }
        }
        iDataUtil.getIDbManager().open();
        //注入入口
        boolean injectSeedState = this.injectSeeds();
        LOG.info("注入种子？" + injectSeedState);
        status = RUNNING;
        LOG.info("爬虫配置完成，开始抓取：" + this.toString());
        /*for (int i = 0; i < siteConfig.getDeepPath(); i++) {
            if (status == STOPED) {
                break;
            }*/
        //LOG.info("start depth " + (i + 1));
        long startTime = System.currentTimeMillis();
        int totalGenerate = fetcher.fetcherStart();
        long endTime = System.currentTimeMillis();

        long costTime = (endTime - startTime) / 1000;
        LOG.info("\ndepth " + (1) + " finish: " +
                "\n              this depth total urls: " + totalGenerate + "" +
                "\n              this depth total time: " + costTime + " seconds");
           /* if (totalGenerate == 0) {
                break;
            }
        }*/
        iDataUtil.getIDbManager().close();
        this.afterStopSpider();
        return true;
    }

    @Override
    public boolean stopSpider() {
        status = STOPED;
        LOG.info("-----------关闭爬虫------------");
        return fetcher.stopFetcher();
    }

    @Override
    public abstract void afterStopSpider();

    @Override
    public void setConfig(SiteConfig config) {
        this.siteConfig = config;
    }
}
