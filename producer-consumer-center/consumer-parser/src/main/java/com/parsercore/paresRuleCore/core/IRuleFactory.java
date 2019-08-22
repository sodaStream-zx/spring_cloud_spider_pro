package com.parsercore.paresRuleCore.core;

/**
 * @author 一杯咖啡
 * @desc 解析器规则获取
 * @createTime 2018-12-25-13:07
 */
@FunctionalInterface
public interface IRuleFactory<T> {
    T getRule(String siteName);
}
