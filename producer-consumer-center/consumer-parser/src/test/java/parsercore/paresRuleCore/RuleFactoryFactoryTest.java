package parsercore.paresRuleCore;

import commoncore.entity.paresEntity.DomainRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import parsercore.paresRuleCore.core.IRuleFactory;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-15-15:38
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RuleFactoryFactoryTest {
    @Autowired
    IRuleFactory<DomainRule> iRuleFactory;

    @Test
    public void getRule() {
        DomainRule domainRule = iRuleFactory.getRule("大渝网");
        System.out.println(domainRule.toString());
        System.out.println(domainRule.toString());
        System.out.println(domainRule.toString());
    }
}