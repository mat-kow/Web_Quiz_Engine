package engine.domain.repo;

import engine.domain.entity.CompletedQuiz;
import engine.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface CompletedQuizRepository extends CrudRepository<CompletedQuiz, Integer> {
    Page<CompletedQuiz> findAllByUserId(Pageable pageable, int userId);

}
