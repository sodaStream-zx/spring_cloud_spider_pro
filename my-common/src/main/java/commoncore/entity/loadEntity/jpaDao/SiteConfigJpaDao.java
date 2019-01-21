package commoncore.entity.loadEntity.jpaDao;

import commoncore.entity.loadEntity.SiteConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * @author Twilight
 * @desc
 * @createTime 2019-01-17-16:43
 */

@Repository
public interface SiteConfigJpaDao extends JpaRepository<SiteConfig, Integer> {
}
