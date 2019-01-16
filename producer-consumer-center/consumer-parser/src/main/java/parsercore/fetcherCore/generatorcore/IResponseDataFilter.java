package parsercore.fetcherCore.generatorcore;

/**
 * @author 一杯咖啡
 * @desc 数据过滤工具
 * @createTime 2018-12-26-14:47
 */
@FunctionalInterface
public interface IResponseDataFilter<T> {
    boolean pass(T data);
}
