package commoncore.Mappers;

import commoncore.entity.loadEntity.MyNew;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Twilight
 * @desc 新闻信息mapper
 * @createTime 2019-01-15-16:39
 */
@Mapper
public interface MyNewMapper {
    Integer save(MyNew myNew);
}
