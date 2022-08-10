package engine.quiz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface QuizPagingAndSortingRepository extends PagingAndSortingRepository<Quiz, Integer> {

    @Transactional
    @Query(value = "SELECT * FROM completed_quiz where user_id = :user_id", nativeQuery = true)
    Page<Object[]> findAllCompleted(@Param("user_id") int userId, Pageable pageable);

}
