package commoncore.entity.requestEntity.entityTools;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-07-22:57
 */
public class RegexRuleTest {
    private RegexRule regexRule = new RegexRule();

    @Test
    public void addContentRegexRule() {
    }

    @Test
    public void satisfyContentRules() {
        //邮箱正则
        regexRule.addContentRegexRule("^\\w+@(\\w+\\.)+\\w+");
        boolean index = regexRule.satisfyContentRules("113983@qq.com");
        Assert.assertTrue("不匹配", index);
    }
}