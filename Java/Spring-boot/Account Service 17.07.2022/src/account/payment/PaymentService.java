package account.payment;

import account.util.Utils;
import account.payment.dto.*;
import account.user.User;
import account.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.DateTimeException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional
    public Feedback saveAll(List<PaymentDataRequest> requests) {
        for (PaymentDataRequest request : requests) {
            YearMonth period = parsePeriod(request.getPeriod());
            if (paymentRepository
                    .findAllByEmployee(request.getEmployee())
                    .stream()
                    .anyMatch(dbP -> dbP.getPeriod().equals(period))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The employee-period pair must be unique!");
            }
            if (!userRepository.existsByEmail(request.getEmployee())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee is not found!");
            }
            paymentRepository.save(new Payment(request.getEmployee().toLowerCase(), period, request.getSalary()));
        }
        return new Feedback("Added successfully!");
    }

    Feedback update(String employee, String strPeriod, long salary) {
        YearMonth period = parsePeriod(strPeriod);
        return paymentRepository.findByEmployeeIgnoreCaseAndPeriod(employee, period)
                .map(payment -> {
                    Payment newPayment = new Payment(employee, period, salary);
                    newPayment.setId(payment.getId());
                    paymentRepository.save(newPayment);
                    return new Feedback("Updated successfully!");
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee is not found!"));
    }

    PaymentInfo getInfoForPeriod(YearMonth period) {
        return paymentRepository.findByEmployeeIgnoreCaseAndPeriod(Utils.getCurrentUser().getUsername(), period)
                .map(payment -> {
                    User employee = userRepository.findUserByEmailIgnoreCase(payment.getEmployee()).orElseThrow();
                    return new PaymentInfo(
                            employee.getName(),
                            employee.getLastname(),
                            payment.getPeriod().format(DateTimeFormatter.ofPattern("MMMM-yyyy")),
                            String.format("%s dollar(s) %s cent(s)",
                                    payment.getSalary() / 100, payment.getSalary() % 100)
                    );
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment not found!"));
    }

    List<PaymentInfo> getInfo() {
        return paymentRepository.findAllByEmployee(Utils.getCurrentUser().getUsername()).stream()
                .sorted(Comparator.comparing(Payment::getPeriod).reversed())
                .map(payment -> {
                    User employee = userRepository.findUserByEmailIgnoreCase(payment.getEmployee()).orElseThrow();
                    return new PaymentInfo(
                            employee.getName(),
                            employee.getLastname(),
                            payment.getPeriod().format(DateTimeFormatter.ofPattern("MMMM-yyyy")),
                            String.format("%s dollar(s) %s cent(s)",
                                    payment.getSalary() / 100, payment.getSalary() % 100)
                    );
                }).collect(Collectors.toList());
    }

    private YearMonth parsePeriod(String period) {
        try {
            return YearMonth.parse(period, DateTimeFormatter.ofPattern("MM-yyyy"));
        } catch (DateTimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong date in request!");
        }
    }
}

