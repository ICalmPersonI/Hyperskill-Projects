package antifraud.transaction;


import antifraud.Util;
import antifraud.dto.transaction.TransactionFeedback;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/antifraud")
class TransactionController {

    private TransactionService service;

    TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/transaction")
    ResponseEntity<?> post(@Valid @RequestBody Transaction transaction) {
        return new ResponseEntity<>(
                service.transaction(transaction),
                Util.JSON_HEADERS,
                HttpStatus.OK);
    }

    @PutMapping("/transaction")
    ResponseEntity<?> put(@Valid @RequestBody TransactionFeedback feedback) {
        return new ResponseEntity<>(service.setFeedback(feedback), Util.JSON_HEADERS, HttpStatus.OK);
    }

    @GetMapping("/history")
    ResponseEntity<?> getAll() {
        return new ResponseEntity<>(service.getAll(), Util.JSON_HEADERS, HttpStatus.OK);
    }

    @GetMapping("/history/{number}")
    ResponseEntity<?> getAllByCardNumber(@PathVariable("number") String number) {
        return new ResponseEntity<>(service.getAllByCardNumber(number), Util.JSON_HEADERS, HttpStatus.OK);
    }
}
