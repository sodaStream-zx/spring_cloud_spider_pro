package spider.spiderCore.iexecutorCom;

import commoncore.entity.requestEntity.CrawlDatum;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-07-15:25
 */
public interface IExecutor<T> {
    /**
     * desc: 执行请求，利用解析器，存储器等工具
     *
     * @Return: T 后续任务泛型
     **/
    T execute(CrawlDatum datum);
}
