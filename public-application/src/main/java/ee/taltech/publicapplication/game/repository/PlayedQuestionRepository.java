package ee.taltech.publicapplication.game.repository;

import ee.taltech.publicapplication.game.model.PlayedQuestion;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

@ReadingConverter
public interface PlayedQuestionRepository extends ReactiveCrudRepository<PlayedQuestion, Long> {
}
