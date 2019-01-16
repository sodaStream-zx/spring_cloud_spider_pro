package spider.entityTools;

import org.junit.Assert;
import org.junit.Test;
import spider.spiderCore.entitys.RegexRule;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-07-22:57
 */
public class RegexRuleTest {
    private RegexRule regexRule = new RegexRule();

    @Test
    public void addContentRegexRule() {
        regexRule.addContentRegexRule("conrule_1");
        Assert.assertEquals(1, regexRule.getContentRegexRules().size());
    }


    @Test
    public void addPickReges() {
        regexRule.addPickReges("pickrule_1");
        Assert.assertEquals(1, regexRule.getPickRegexs().size());
    }

    @Test
    public void addContentRegexRule2() {
    }

    @Test
    public void satisfyPickRegex() {
    }

    @Test
    public void satisfyContentRegex() {
        //邮箱正则
        regexRule.addContentRegexRule("^\\w+@(\\w+\\.)+\\w+");
        boolean index = regexRule.satisfyContentRegex("113983@qq.com");
        Assert.assertTrue("不匹配", index);
    }

    @Test
    public void info() {
    }
}