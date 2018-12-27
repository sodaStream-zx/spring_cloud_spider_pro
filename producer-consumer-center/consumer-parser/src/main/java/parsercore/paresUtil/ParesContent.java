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
     * Description: 数据解析方法
     * @param page 数据页面
     * @param domainRule 解析器规则
     * @return T 泛型 后期可解析出不同内容
     **/
    T paresContent(ResponsePage page, DomainRule domainRule);
}
