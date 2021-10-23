package ee.taltech.backoffice.game.repository;

import ee.taltech.backoffice.game.model.Quiz;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends CrudRepository<Quiz, Long> {

    @Query("SELECT * FROM kahoot.quiz AS q WHERE q.name = :name")
    Optional<Quiz> findByName(@Param("name") String name);

    @Query("SELECT * FROM kahoot.quiz AS q WHERE q.author_id = :authorId AND q.id = :quizId")
    Optional<Quiz> findByIdAndAuthorId(@Param("quizId") Long quizId, @Param("authorId") Long authorId);

    @Query("SELECT * FROM kahoot.quiz AS q WHERE q.author_id = :authorId")
    List<Quiz> findByAuthor(@Param("authorId") Long authorId);

}
