package ee.taltech.publicapplication.game.repository;

import ee.taltech.publicapplication.game.model.Answer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface AnswerRepository extends ReactiveCrudRepository<Answer, Long> {

    Flux<Answer> findAllByQuestionIdIn(List<Long> ids);

    Flux<Answer> findAllByQuestionId(Long id);

}
