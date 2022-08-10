package platform.code;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.stream.StreamSupport;

@Component
public class CodeTimer {
    private CodeRepository repository;

    @Autowired
    CodeTimer(CodeRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedDelay = 800)
    void timer() {
        StreamSupport.stream(repository.findAll().spliterator(), false)
                .peek(e -> {
                    if (e.isTimeLimit()) {
                        repository.updateTimeById(e.getId());
                    }
                }).forEach(e -> {
                    if (e.getTime() < 1 && e.isTimeLimit()) {
                        repository.deleteById(e.getId());
                    }
                });
    }
}

