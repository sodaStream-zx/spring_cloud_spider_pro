package spider.spiderCore.crawler;

import commoncore.entity.requestEntity.CrawlDatum;
import commoncore.entity.requestEntity.CrawlDatums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spider.spiderCore.fetchercore.Fetcher;
import spider.spiderCore.idbcore.IDataUtil;
import spider.spiderCore.iexecutorCom.INextFilter;

/**
 * desc:单层爬虫
 *
 * @author 一杯咖啡
 */
public class Crawler {

    private static final Logger LOG = LoggerFactory.getLogger(Crawler.class);

    public Crawler() {
    }

    private int status;

    public final static int RUNNING = 1;
    public final static int STOPED = 2;
    protected boolean resumable = false;
    protected int threads = 50;


    protected CrawlDatums seeds = new CrawlDatums();
    protected CrawlDatums forcedSeeds = new CrawlDatums();
    protected Fetcher fetcher;
    protected INextFilter INextFilter = null;
    protected IDataUtil iDataUtil;


    protected void injectList() throws Exception {
        iDataUtil.getIDbWritor().injectList(seeds, false);
    }

    protected void injectForcedSeeds() throws Exception {
        iDataUtil.getIDbWritor().injectList(forcedSeeds, true);
    }

    /**
     * 开始爬取，迭代次数为depth
     *
     * @param depth 迭代次数
     * @throws Exception 异常
     */
    public void start(int depth) throws Exception {
        LOG.info(this.toString());
        if (!resumable) {
            if (iDataUtil.getIDbManager().isDBExists()) {
                iDataUtil.getIDbManager().clear();
            }
            if (seeds.isEmpty() && forcedSeeds.isEmpty()) {
                LOG.error("error:Please add at least one seed");
                return;
            }
        }
        iDataUtil.getIDbManager().open();

        if (!seeds.isEmpty()) {
            injectList();
        }
        if (!forcedSeeds.isEmpty()) {
            injectForcedSeeds();
        }
        status = RUNNING;
        for (int i = 0; i < depth; i++) {
            if (status == STOPED) {
                break;
            }
            LOG.info("start depth " + (i + 1));
            long startTime = System.currentTimeMillis();
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
     * 添加种子任务
     *
     * @param datum 种子任务
     * @param force 如果添加的种子是已爬取的任务，当force为true时，会强制注入种子，当force为false时，会忽略该种子
     */
    public void addSeed(CrawlDatum datum, boolean force) {
        addSeedAndReturn(datum, force);
    }

    /**
     * 等同于 addSeed(datum, false)
     *
     * @param datum 种子任务
     */
    public void addSeed(CrawlDatum datum) {
        addSeedAndReturn(datum);
    }

    /**
     * 添加种子集合
     *
     * @param datums 种子集合
     * @param force  如果添加的种子是已爬取的任务，当force为true时，会强制注入种子，当force为false时，会忽略该种子
     */
    public void addSeed(CrawlDatums datums, boolean force) {
        addSeedAndReturn(datums, force);
    }

    /**
     * 等同于 addSeed(datums,false)
     *
     * @param datums 种子任务集合
     */
    public void addSeed(CrawlDatums datums) {
        addSeedAndReturn(datums);
    }


    /**
     * 与addSeed(CrawlDatums datums, boolean force) 类似
     *
     * @param links 种子URL集合
     * @param force 是否强制注入
     */
    public void addSeed(Iterable<String> links, boolean force) {
        addSeedAndReturn(links, force);
    }


    /**
     * 与addSeed(CrawlDatums datums)类似
     *
     * @param links 种子URL集合
     */
    public void addSeed(Iterable<String> links) {
        addSeedAndReturn(links);
    }

    /**
     * 与addSeed(CrawlDatum datum, boolean force)类似
     *
     * @param url   种子URL
     * @param force 是否强制注入
     */
    public void addSeed(String url, boolean force) {
        addSeedAndReturn(url, force);
    }

    /**
     * 与addSeed(CrawlDatum datum)类似
     *
     * @param url 种子URL
     */
    public void addSeed(String url) {
        addSeedAndReturn(url);
    }

    public CrawlDatum addSeedAndReturn(CrawlDatum datum, boolean force) {
        if (force) {
            forcedSeeds.add(datum);
        } else {
            seeds.add(datum);
        }
        return datum;
    }

    public CrawlDatum addSeedAndReturn(CrawlDatum datum) {
        return addSeedAndReturn(datum, false);
    }

    public CrawlDatum addSeedAndReturn(String url, boolean force) {
        CrawlDatum datum = new CrawlDatum(url);
        return addSeedAndReturn(datum, force);
    }

    public CrawlDatum addSeedAndReturn(String url) {
        return addSeedAndReturn(url, false);
    }

    public CrawlDatums addSeedAndReturn(Iterable<String> links, boolean force) {
        CrawlDatums datums = new CrawlDatums(links);
        return addSeedAndReturn(datums, force);
    }

    public CrawlDatums addSeedAndReturn(Iterable<String> links) {
        return addSeedAndReturn(links, false);
    }

    public CrawlDatums addSeedAndReturn(CrawlDatums datums, boolean force) {
        if (force) {
            forcedSeeds.add(datums);
        } else {
            seeds.add(datums);
        }
        return datums;
    }

    public CrawlDatums addSeedAndReturn(CrawlDatums datums) {
        return addSeedAndReturn(datums, false);
    }

    /**
     * 返回是否断点爬取
     *
     * @return 是否断点爬取
     */
    public boolean isResumable() {
        return resumable;
    }

    /**
     * 设置是否断点爬取
     *
     * @param resumable 是否断点爬取
     */
    public void setResumable(boolean resumable) {
        this.resumable = resumable;
    }


    public INextFilter getINextFilter() {
        return INextFilter;
    }

    public void setINextFilter(INextFilter INextFilter) {
        this.INextFilter = INextFilter;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setiDataUtil(IDataUtil iDataUtil) {
        this.iDataUtil = iDataUtil;
    }
}
