package spider.spiderCore.crawler;

import commoncore.entity.configEntity.SiteConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spider.spiderCore.crawldb.IDataUtil;
import spider.spiderCore.fetcher.Fetcher;

/**
 * desc:任务注入工具
 *
 * @author 一杯咖啡
 */
@Component
public class SpiderUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SpiderUtils.class);

    public SpiderUtils() {
    }

    private int status;

    public final static int RUNNING = 1;
    public final static int STOPED = 2;
    private boolean resumable = false;
    private int threads = 50;
    @Autowired
    private Fetcher fetcher;
    @Autowired
    private IDataUtil iDataUtil;

    @Autowired
    private SeedData seedData;
    @Autowired
    private RegexRuleData regexRuleData;

    @Autowired
    private SiteConfig siteConfig;


    public void start(int depth) throws Exception {
        LOG.info(this.toString());
        if (!resumable) {
            if (iDataUtil.getIDbManager().isDBExists()) {
                iDataUtil.getIDbManager().clear();
            }
            if (seedData.getSeeds().isEmpty() && seedData.getForcedSeeds().isEmpty()) {
                LOG.error("error:Please add at least one seed");
                return;
            }
        }
        iDataUtil.getIDbManager().open();

        if (!seedData.getSeeds().isEmpty()) {
            injectList();
        }
        if (!seedData.getForcedSeeds().isEmpty()) {
            injectForcedSeeds();
        }
        status = RUNNING;
        for (int i = 0; i < depth; i++) {
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
        afterStop();
    }

    public void afterStop() {
    }

    /**
     * 停止爬虫
     */
    public void stop() {
        status = STOPED;
        fetcher.stopFetcher();
    }

    /**
     * desc: 添加种子到数据库
     **/
    public void injectList() throws Exception {
        iDataUtil.getIDbWritor().injectList(seedData.getSeeds(), false);
    }

    public void injectForcedSeeds() throws Exception {
        iDataUtil.getIDbWritor().injectList(seedData.getForcedSeeds(), true);
    }

}
