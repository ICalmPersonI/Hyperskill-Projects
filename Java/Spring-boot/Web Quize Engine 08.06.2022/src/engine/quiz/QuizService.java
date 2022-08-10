package engine.quiz;

import engine.forms.CompletedPage;
import engine.forms.QuizPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class QuizService {

    private QuizRepository repository;

    private QuizPagingAndSortingRepository PASRepository;

    @Autowired
    QuizService(QuizRepository repository, QuizPagingAndSortingRepository PASRepository) {
        this.repository = repository;
        this.PASRepository = PASRepository;
    }

    Quiz add(Quiz quiz) {
        return repository.save(quiz);
    }

    boolean update(int quizId, int userId) {
        return repository.findById(quizId).map(value -> {
            List<CompletedQuiz> newList = value.getCompletedQuizList();
            newList.add(new CompletedQuiz(userId, LocalDateTime.now(), value));
            value.setCompletedQuizList(newList);
            repository.save(value);
            return true;
        }).orElseThrow(NoSuchElementException::new);
    }

    Optional<Quiz> get(int id) {
        return repository.findById(id);
    }

    void delete(int id) {
        repository.deleteById(id);
    }

    QuizPage getAll(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 10);
        Page<Quiz> page = PASRepository.findAll(pageable);
        return page.getTotalPages() == 0 ?
                new QuizPage() :
                new QuizPage(page.getTotalPages(), (int) page.getTotalElements(), page.stream().toArray(Quiz[]::new));
    }

    CompletedPage getAllCompletedForUser(int pageNo, int userId) {
        Sort sort = Sort.by(Sort.Order.desc("completed_at"));
        Pageable pageable = PageRequest.of(pageNo, 10, sort);
        Page<Object[]> page = PASRepository.findAllCompleted(userId, pageable);
        return page.getTotalPages() == 0 ?
                new CompletedPage() :
                new CompletedPage(
                        page.getTotalPages(),
                        (int) page.getTotalElements(),
                        page.stream()
                                .map(e -> new CompletedPage.Content((int) e[3], ((Timestamp) e[1]).toLocalDateTime()))
                                .toArray(CompletedPage.Content[]::new)
                );

    }
}
