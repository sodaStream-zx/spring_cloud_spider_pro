package parsercore.dbUtils.reposi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parsercore.paresEntity.DomainRule;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-15-16:40
 */
@Repository
public interface DomainRuleDao extends JpaRepository<DomainRule, Integer> {
}
