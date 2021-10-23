package ee.taltech.backoffice.game.repository;

import ee.taltech.backoffice.game.model.Answer;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Long> {

    @Query("SELECT * FROM kahoot.answer AS a WHERE a.text = :text")
    Optional<Answer> findByText(@Param("text") String text);

    @Query("SELECT * FROM kahoot.answer AS a WHERE a.question_id = :questionId")
    List<Answer> findByQuestionId(@Param("questionId") Long questionId);
}
