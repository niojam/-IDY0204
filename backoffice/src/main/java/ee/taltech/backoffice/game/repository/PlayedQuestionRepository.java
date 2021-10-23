package ee.taltech.backoffice.game.repository;

import ee.taltech.backoffice.game.model.PlayedQuestion;
import ee.taltech.backoffice.game.model.dto.QuestionDetails;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayedQuestionRepository extends CrudRepository<PlayedQuestion, Long> {

    @Query(value = "SELECT q.id, q.title, q.question_type FROM kahoot.played_question AS q WHERE q.quiz_id = :quizId")
    List<QuestionDetails> findQuestionDetails(@Param("quizId") Long quizId);

    @Query("SELECT * FROM kahoot.played_question AS q WHERE q.quiz_id = :quizId")
    List<PlayedQuestion> findByQuizId(@Param("quizId") Long quizId);

}
