package ee.taltech.publicapplication.game.repository;

import ee.taltech.publicapplication.game.model.PlayedQuiz;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayedQuizRepository extends ReactiveCrudRepository<PlayedQuiz, Long> {
}
