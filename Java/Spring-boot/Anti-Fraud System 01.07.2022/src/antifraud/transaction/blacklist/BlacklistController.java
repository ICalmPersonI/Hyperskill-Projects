package antifraud.transaction.blacklist;

import antifraud.Util;
import antifraud.dto.card.DeleteCardResponse;
import antifraud.dto.ip.DeleteIPResponse;
import antifraud.transaction.blacklist.card.Card;
import antifraud.transaction.blacklist.ip.IP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/antifraud")
public class BlacklistController {

    private BlacklistService service;

    BlacklistController(BlacklistService service) {
        this.service = service;
    }

    @PostMapping("/suspicious-ip")
    ResponseEntity<?> saveIP(@Valid @RequestBody IP ip) {
        return new ResponseEntity<>(service.saveIP(ip), Util.JSON_HEADERS, HttpStatus.OK);
    }

    @GetMapping("/suspicious-ip")
    ResponseEntity<?> getAllIPs() {
        return new ResponseEntity<>(service.getAllIps(), Util.JSON_HEADERS, HttpStatus.OK);
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    ResponseEntity<?> deleteIP(@PathVariable("ip") String ip) {
        service.deleteIP(ip);
        return new ResponseEntity<>(new DeleteIPResponse(ip), Util.JSON_HEADERS, HttpStatus.OK);
    }

    @PostMapping("/stolencard")
    ResponseEntity<?> saveCard(@Valid @RequestBody Card card) {
        return new ResponseEntity<>(service.saveCard(card), Util.JSON_HEADERS, HttpStatus.OK);
    }

    @GetMapping("/stolencard")
    ResponseEntity<?> getAllCards() {
        return new ResponseEntity<>(service.getAllCards(), Util.JSON_HEADERS, HttpStatus.OK);
    }

    @DeleteMapping("/stolencard/{number}")
    ResponseEntity<?> deleteCard(@PathVariable("number") String cardNumber) {
        service.deleteCard(cardNumber);
        return new ResponseEntity<>(new DeleteCardResponse(cardNumber), Util.JSON_HEADERS, HttpStatus.OK);
    }
}
