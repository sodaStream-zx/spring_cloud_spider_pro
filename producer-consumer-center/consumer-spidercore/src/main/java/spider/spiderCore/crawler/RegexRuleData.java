package spider.spiderCore.crawler;

import commoncore.entity.requestEntity.entityTools.RegexRule;
import org.springframework.stereotype.Component;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-07-16:29
 */
@Component
public class RegexRuleData {
    private RegexRule regexRule = new RegexRule();

    /**
     * 添加URL正则约束
     *
     * @param urlRegex URL正则约束
     */
    public void addRegex(String urlRegex) {
        regexRule.addRule(urlRegex);
    }

    public RegexRule getRegexRule() {
        return regexRule;
    }

    public void setRegexRule(RegexRule regexRule) {
        this.regexRule = regexRule;
    }

    @Override
    public String toString() {
        return "RegexRuleData{" +
                "regexRule=" + regexRule +
                '}';
    }
}
