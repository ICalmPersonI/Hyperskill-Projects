package antifraud.transaction.limit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LimitService {

    private LimitRepository repository;

    @Autowired
    LimitService(LimitRepository repository) {
        this.repository = repository;
    }

    public void save(long allowed, long manual) {
        Limit newLimit = new Limit(allowed, manual);
        newLimit.setId(1);
        repository.save(newLimit);
    }

    public Optional<Limit> load() {
        return repository.findTopByOrderByIdDesc();
    }
}
