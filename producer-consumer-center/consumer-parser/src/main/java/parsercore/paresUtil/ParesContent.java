package parsercore.paresUtil;


import commoncore.entity.paresEntity.DomainRule;
import commoncore.entity.responseEntity.ResponsePage;

/**
 * 实现该接口
 * desc: visitor 使用该工具解析正文，提取自定义需要的内容
 * @author 一杯咖啡
 * */
public interface ParesContent<T> {
    /**
     * @Description: [页面内容提取]
     * @param page 当前页面
     */
    T paresContent(ResponsePage page, DomainRule domainRule);
}
