package com.parsercore.dbUtils.mysqlDao;

/**
 * desc: mysql数据存取接口
 *
 * @author 一杯咖啡
 **/
public interface IMysqlDao<T, O> {
    void insertNew(T entity);

    O getDomainRuleFromMysql(String siteName);
}
