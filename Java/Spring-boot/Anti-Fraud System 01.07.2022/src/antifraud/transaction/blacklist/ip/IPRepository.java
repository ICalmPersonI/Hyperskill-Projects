package antifraud.transaction.blacklist.ip;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPRepository extends CrudRepository<IP, Integer> {

    boolean existsByIp(String ip);

    Optional<IP> findByIp(String ip);
}
