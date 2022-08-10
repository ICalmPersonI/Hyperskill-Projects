package engine.quiz;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface QuizRepository extends CrudRepository<Quiz, Integer> {


    @Query(value = "SELECT * FROM completed_quiz;", nativeQuery = true)
    List<Object[]> findAllCompleted();
}
