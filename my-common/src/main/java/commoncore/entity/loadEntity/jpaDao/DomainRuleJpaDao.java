package commoncore.entity.loadEntity.jpaDao;

import commoncore.entity.loadEntity.DomainRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-15-16:40
 */
@Repository
public interface DomainRuleJpaDao extends JpaRepository<DomainRule, Integer> {
}
