package account.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class EventService {

    @Autowired
    private EventRepository repository;

    public void save(String action, String subject, String object, String path) {
        repository.save(new Event(LocalDateTime.now().toString(), action, subject, object, path));
    }

    List<Event> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
    }

}
