package ee.taltech.publicapplication.game.repository;

import ee.taltech.publicapplication.game.model.Question;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface QuestionRepository extends ReactiveCrudRepository<Question, Long> {


    Flux<Question> findAllByQuizIdOrderByIdDesc(Long quizId);

}
