package commoncore.entity.paresEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-15-16:40
 */
@Repository
public interface DomainRuleDao extends JpaRepository<DomainRule, Integer> {
}
