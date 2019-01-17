package parsercore.paresEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-15-16:39
 */
@Repository
public interface MyNewDao extends JpaRepository<MyNew, Integer> {
}
