package parsercore.paresrule.core;

import commoncore.entity.paresEntity.DomainRule;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-25-13:07
 */
public interface IRule {
    DomainRule getRule(String siteName);
}
