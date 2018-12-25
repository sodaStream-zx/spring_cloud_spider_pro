package parsercore.dbUtils.Istore;

import commoncore.entity.paresEntity.DomainRule;

/**
 * desc: mysql数据存取接口
 * @author 一杯咖啡
 **/
public interface IMysqlDao<T> {
    void insertNew(T entity);
    DomainRule getDomainRuleFromMysql(String siteName);

}
