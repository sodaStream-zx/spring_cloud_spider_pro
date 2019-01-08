package spider.myspider;


import commoncore.entity.configEntity.SiteConfig;
import commoncore.entity.httpEntity.ResponsePage;
import commoncore.parseTools.StringSplitUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spider.spiderCore.crawldb.IDataUtil;
import spider.spiderCore.crawler.AbstractAutoParseCrawler;
import spider.spiderCore.fetcher.Fetcher;
import spider.spiderCore.fetcher.IFetcherTools.TransferToParser;
import spider.spiderCore.http.ISendRequest;

import java.util.Arrays;


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
    /**
     * urlRules url 解析正则表达式
     * seeds 入口 url
     * conPickRules 正文提取正则表达式
     */
    private String[] urlRules;
    private String[] seeds;
    private String[] conPickRules;
    @Value(value = "${spider.totalThreads}")
    private int totalThread;
    @Autowired
    private TransferToParser<ResponsePage> transferToParser;

    @Autowired
    public MySpider(IDataUtil iDataUtil, Fetcher fetcher) {
        this.fetcher = fetcher;
        this.iDataUtil = iDataUtil;
    }

    /**
     * @param siteConfig   网站配置信息
     * @param iSendRequest 自定义请求工具 需实现requestor接口
     *                     desc :初始化爬虫组件
     */
    public void initMySpider(SiteConfig siteConfig, ISendRequest<ResponsePage> iSendRequest) {
        this.siteconfig = siteConfig;
        this.iSendRequest = iSendRequest;
        urlRules = StringSplitUtil.splitRule(siteconfig.getUrlPares());
        seeds = StringSplitUtil.splitRule(siteconfig.getSeeds());
        conPickRules = StringSplitUtil.splitRule(siteconfig.getPageParse());
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
            e.printStackTrace();
            LOG.error("开启爬虫失败");
        }
    }

    /**
     * desc: 爬虫完成后执行
     */
    @Override
    public void afterStop() {
        LOG.info("总提取量归零");
        iDataUtil.getIGenerator().clear();
        LOG.info("等待10秒 继续下一任务--------------------------");
        //System.exit(0);
    }


    @Override
    public String toString() {
        return "MySpider{" +
                "siteconfig=" + siteconfig +
                ", urlRules=" + Arrays.toString(urlRules) +
                ", seeds=" + Arrays.toString(seeds) +
                ", conPickRules=" + Arrays.toString(conPickRules) +
                ", totalThread=" + totalThread +
                ", transferToParser=" + transferToParser +
                '}';
    }
}
