package parsercore.dbUtils.gainDao.IGain;


import commoncore.entity.paresEntity.DomainRule;
import commoncore.entity.responseEntity.ResponseData;
import parsercore.fetchercore.generatorcore.IGenerator;

/**
 * @author 一杯咖啡
 * @desc 获取响应数据接口
 * @createTime 2018-12-21-15:38
 */
public interface IRedisDao extends IGenerator<ResponseData> {
    /**
     * desc: redis 数据操作
     **/
    ResponseData getResponseDataFromRedis(String listKey);

    DomainRule getDomainRuleFromRedis(String siteName);

    void addDomainRuleToRedis(DomainRule domainRule);
}
