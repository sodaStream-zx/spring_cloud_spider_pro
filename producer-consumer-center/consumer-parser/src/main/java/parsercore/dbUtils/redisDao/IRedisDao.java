package parsercore.dbUtils.redisDao;


import commoncore.entity.paresEntity.DomainRule;

/**
 * @author 一杯咖啡
 * @desc 获取响应数据接口
 * @createTime 2018-12-21-15:38
 */
public interface IRedisDao {
    /**
     * desc: redis 数据操作
     **/
    DomainRule getDomainRuleFromRedis(String siteName);

    void addDomainRuleToRedis(DomainRule domainRule);
}
