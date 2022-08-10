package antifraud.transaction.blacklist.card;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends CrudRepository<Card, Integer> {

    boolean existsByNumber(String number);

    Optional<Card> findByNumber(String number);
}
