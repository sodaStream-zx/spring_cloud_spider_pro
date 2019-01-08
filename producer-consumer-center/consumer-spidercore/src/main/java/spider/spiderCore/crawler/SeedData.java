package spider.spiderCore.crawler;

import commoncore.entity.requestEntity.CrawlDatum;
import commoncore.entity.requestEntity.CrawlDatums;
import org.springframework.stereotype.Component;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-07-16:27
 */
@Component
public class SeedData {
    private CrawlDatums seeds = new CrawlDatums();
    private CrawlDatums forcedSeeds = new CrawlDatums();

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

    @Override
    public String toString() {
        return "SeedData{" +
                "seeds=" + seeds +
                ", forcedSeeds=" + forcedSeeds +
                '}';
    }

    public CrawlDatums getSeeds() {
        return seeds;
    }

    public void setSeeds(CrawlDatums seeds) {
        this.seeds = seeds;
    }

    public CrawlDatums getForcedSeeds() {
        return forcedSeeds;
    }

    public void setForcedSeeds(CrawlDatums forcedSeeds) {
        this.forcedSeeds = forcedSeeds;
    }
}
