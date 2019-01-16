package parsercore.fetcherCore.generatorcore;

/**
 * @author 一杯咖啡
 * @desc 数据提取工具
 * @createTime 2018-12-26-14:46
 */
@FunctionalInterface
public interface IParseDataGenerator<T> {
    T getData();
}
