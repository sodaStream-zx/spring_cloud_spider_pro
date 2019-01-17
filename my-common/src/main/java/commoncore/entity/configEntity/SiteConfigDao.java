package commoncore.entity.configEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/*
 * @author Twilight
 * @desc
 * @createTime 2019-01-17-16:43
 */

@Repository
@Component
public interface SiteConfigDao extends JpaRepository<SiteConfig, Integer> {
}
