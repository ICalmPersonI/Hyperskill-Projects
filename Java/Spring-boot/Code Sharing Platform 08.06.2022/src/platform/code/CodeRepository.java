package platform.code;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CodeRepository extends CrudRepository<Code, String> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE Codes SET views = views - 1 WHERE id = :id", nativeQuery = true)
    void updateViewsById(@Param("id") String id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Codes SET time = time - 1 WHERE id = :id", nativeQuery = true)
    void updateTimeById(@Param("id") String id);
}
