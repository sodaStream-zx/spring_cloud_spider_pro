package commoncore.entity.loadEntity.jpaDao;

import commoncore.entity.loadEntity.WebSiteConf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-21-16:25
 */
@Repository
public interface WebSiteConfJpaDao extends JpaRepository<WebSiteConf, Integer> {
}
