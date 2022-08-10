package platform.code;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CodeService {

    private CodeRepository repository;

    @Autowired
    CodeService(CodeRepository repository) {
        this.repository = repository;
    }

    Optional<Code> get(String id) {
        Optional<Code> optionalCode = repository.findById(id);
        if (optionalCode.isPresent()) {
            Code code = optionalCode.get();
            if (code.isViewLimit()) {
                repository.updateViewsById(code.getId());
                code.setViews(code.getViews() - 1);
            }
            if (code.isViewLimit() && code.getViews() < 1) {
                repository.deleteById(code.getId());
            }
            return optionalCode;
        }
        return Optional.empty();
    }

    String add(Code code) {
        return repository.save(code).getId();
    }

    ArrayList<Code> getLatest() {
        List<Code> codes = StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.reverse(codes);
        return codes.stream()
                .filter(e -> !(e.isTimeLimit() || e.isViewLimit()))
                .limit(10)
                .collect(Collectors.toCollection(ArrayList::new));
    }


}
