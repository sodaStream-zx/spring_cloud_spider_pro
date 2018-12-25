package commoncore.parseTools;

import org.springframework.stereotype.Component;

/**
 * @author 一杯咖啡
 * @Title：正文解析工具合集
 * @Description:
 */
@Component
public class ParesUtil {
    private Selectors selectors = new Selectors();
    private RulesSplitUtil rulesSplitUtil = new RulesSplitUtil();
    private ParesCounter paresCounter = new ParesCounter();
    private TimeFilter timeFilter = new TimeFilter();

    public Selectors getSelectors() {
        return selectors;
    }

    public RulesSplitUtil getRulesSplitUtil() {
        return rulesSplitUtil;
    }

    public ParesCounter getParesCounter() {
        return paresCounter;
    }

    public TimeFilter getTimeFilter() {
        return timeFilter;
    }

    @Override
    public String toString() {
        return "ParesUitl{" +
                "\n selectors=" + selectors.getClass().getName() +
                "\n rulesSplitUtil=" + rulesSplitUtil.getClass().getName() +
                "\n paresCounter=" + paresCounter.getClass().getName() +
                "\n timeFilter=" + timeFilter.getClass().getName() +
                '}';
    }
}
