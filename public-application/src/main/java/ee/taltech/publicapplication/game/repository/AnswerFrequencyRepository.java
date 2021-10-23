package ee.taltech.publicapplication.game.repository;

import ee.taltech.publicapplication.game.model.AnswerFrequency;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerFrequencyRepository extends ReactiveCrudRepository<AnswerFrequency, Long> {
}
