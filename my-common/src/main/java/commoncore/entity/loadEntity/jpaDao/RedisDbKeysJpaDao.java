package commoncore.entity.loadEntity.jpaDao;

import commoncore.entity.loadEntity.RedisDbKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-22-15:23
 */
@Repository
public interface RedisDbKeysJpaDao extends JpaRepository<RedisDbKeys, Integer> {
}
