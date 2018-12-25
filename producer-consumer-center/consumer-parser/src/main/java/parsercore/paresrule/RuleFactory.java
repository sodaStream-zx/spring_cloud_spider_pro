package parsercore.paresrule;

import commoncore.entity.paresEntity.DomainRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import parsercore.dbUtils.MysqlDao;
import parsercore.paresrule.core.IRule;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-25-13:07
 */
@Component
public class RuleFactory implements IRule {
    @Autowired
    private MysqlDao dataStoreTool;

    @Override
    public DomainRule getRule(String siteName) {
        return null;
    }

}
