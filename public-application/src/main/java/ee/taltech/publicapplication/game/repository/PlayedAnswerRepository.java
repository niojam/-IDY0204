package ee.taltech.publicapplication.game.repository;

import ee.taltech.publicapplication.game.model.PlayedAnswer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayedAnswerRepository extends ReactiveCrudRepository<PlayedAnswer, Long> {
}
