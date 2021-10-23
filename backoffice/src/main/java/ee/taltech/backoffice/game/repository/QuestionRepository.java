package ee.taltech.backoffice.game.repository;

import ee.taltech.backoffice.game.model.Question;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {

    @Query("SELECT * FROM kahoot.question AS q WHERE q.title = :title")
    Optional<Question> findByTitle(@Param("title") String title);

    @Query("SELECT * FROM kahoot.question AS q WHERE q.quiz_id = :quizId")
    List<Question> findByQuizId(@Param("quizId") Long quizId);

}
