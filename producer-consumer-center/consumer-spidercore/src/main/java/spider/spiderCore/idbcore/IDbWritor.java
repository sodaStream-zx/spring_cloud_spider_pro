package spider.spiderCore.idbcore;

/**
 * @author 一杯咖啡
 * @desc 数据库写入工具
 * @createTime 2019-01-04-12:58
 */
public interface IDbWritor<T, V> {
    /**
     * desc: 写入入口url
     **/
    void inject(T datum, boolean force) throws Exception;

    /**
     * desc: 写入入口url
     **/
    void injectList(V datums, boolean force) throws Exception;

    /**
     * desc: 初始化写入工具
     **/
    void initSegmentWriter();

    /**
     * desc: 写入已抓取任务
     **/
    void writeFetchSegment(T fetchDatum);

    /**
     * desc: 写入解析后待抓取数据
     **/
    void writeParseSegment(V parseDatums);

    /**
     * desc: 关闭写入工具
     **/
    void closeSegmentWriter();
}
