package spider.spiderCore.crawldb;

public interface IDataBase<T> {
    /**
     *总任务队列
     */
    T getCrawlDB();
    /**
     *入口任务
     */
    T getFetchDB();
    /**
     *解析入口生成后续任务
     */
    T getLinkDB();
    /**
     *重试任务
     */
    T getRedirectDB();
    void clear();
}
