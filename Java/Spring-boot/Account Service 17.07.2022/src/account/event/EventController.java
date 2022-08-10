package account.event;

import account.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {

    @Autowired
    private EventService service;

    @GetMapping("/api/security/events")
    ResponseEntity<?> getEvents() {
        return new ResponseEntity<>(service.getAll(), Utils.JSON_HEADERS, HttpStatus.OK);
    }
}
