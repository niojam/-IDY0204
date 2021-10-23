package ee.taltech.backoffice.game.repository;

import ee.taltech.backoffice.game.model.PlayedQuiz;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayedQuizRepository extends CrudRepository<PlayedQuiz, Long> {
}
