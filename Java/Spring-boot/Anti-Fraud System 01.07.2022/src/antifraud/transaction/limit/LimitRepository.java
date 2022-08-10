package antifraud.transaction.limit;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LimitRepository extends CrudRepository<Limit, Integer> {

    Optional<Limit> findTopByOrderByIdDesc();
}
