package parsercore.paresUtil;


/**
 * 实现该接口
 * desc: visitor 使用该工具解析正文，提取自定义需要的内容
 * @author 一杯咖啡
 * */
public interface IParseProcess<T, D, R> {
    /**
     * Description: 数据解析方法
     * @param data 数据页面
     * @param rule 解析器规则
     * @return T 泛型 后期可解析出不同内容
     **/
    T paresContent(D data, R rule);
}
