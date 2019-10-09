package commoncore.Mappers;

import commoncore.entity.loadEntity.UrlRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Twilight
 * @desc url规则mapper
 * @createTime 2019-01-21-16:25
 */
@Mapper
public interface UrlRuleMapper {

    List<UrlRule> findAll();
}
