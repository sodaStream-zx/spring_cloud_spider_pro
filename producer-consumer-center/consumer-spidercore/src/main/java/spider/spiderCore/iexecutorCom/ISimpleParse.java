package spider.spiderCore.iexecutorCom;

import commoncore.entity.httpEntity.ResponsePage;
import spider.spiderCore.entitys.CrawlDatums;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-07-15:23
 */
public interface ISimpleParse {
    CrawlDatums parseLinks(ResponsePage responsePage);
}
