package com.parsercore.parseExecutor;


/**
 * @author 一杯咖啡
 * @desc 解析器启动接口
 * @createTime 2018-12-21-15:51
 */
public interface IParesExecutor<D> {
    /**
     * desc:解析传入的数据
     * @param data
     **/
    void parseRun(D data);
}
