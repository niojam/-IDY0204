package ee.taltech.backoffice.game.repository;

import ee.taltech.backoffice.game.model.PlayedAnswer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayedAnswerRepository extends CrudRepository<PlayedAnswer, Long> {
}
