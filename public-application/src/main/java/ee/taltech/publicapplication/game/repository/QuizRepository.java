package ee.taltech.publicapplication.game.repository;

import ee.taltech.publicapplication.game.model.Quiz;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends ReactiveCrudRepository<Quiz, Long> {
}
