package commoncore.Mappers;

import commoncore.entity.loadEntity.DomainRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Twilight
 * @desc 解析器规则mapper
 * @createTime 2019-01-15-16:40
 */
@Mapper
public interface DomainRuleMapper {
    @Select(value = "")
    List<DomainRule> findAll();

}
