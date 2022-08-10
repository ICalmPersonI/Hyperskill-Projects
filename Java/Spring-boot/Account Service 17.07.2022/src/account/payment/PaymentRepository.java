package account.payment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {

    Optional<Payment> findByEmployeeIgnoreCaseAndPeriod(String employee, YearMonth period);
    List<Payment> findAllByEmployee(String employee);
}
