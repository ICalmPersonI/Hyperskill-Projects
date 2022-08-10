package account.payment;

import account.util.Utils;
import account.payment.dto.PaymentDataRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.DateTimeException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class PaymentController {

    @Autowired
    PaymentService service;

    @GetMapping("/api/empl/payment")
    ResponseEntity<?> getAccess(@RequestParam(value = "period", required = false) String period) {
        try {
            return new ResponseEntity<>(
                    period == null ?
                            service.getInfo() :
                            service.getInfoForPeriod(YearMonth.parse(period, DateTimeFormatter.ofPattern("MM-yyyy"))),
                    Utils.JSON_HEADERS,
                    HttpStatus.OK);
        } catch (DateTimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong date in request!");
        }
    }

    @PostMapping("/api/acct/payments")
    ResponseEntity<?> uploadPayrolls(@RequestBody List<@Valid PaymentDataRequest> requests, Errors errors) {
        if (errors.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        return new ResponseEntity<>(service.saveAll(requests), Utils.JSON_HEADERS, HttpStatus.OK);
    }

    @PutMapping("api/acct/payments")
    ResponseEntity<?> uploadInformation(@Valid @RequestBody PaymentDataRequest request, Errors errors) {
        if (errors.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        return new ResponseEntity<>(
                service.update(request.getEmployee(), request.getPeriod(), request.getSalary()),
                Utils.JSON_HEADERS,
                HttpStatus.OK);
    }
}
