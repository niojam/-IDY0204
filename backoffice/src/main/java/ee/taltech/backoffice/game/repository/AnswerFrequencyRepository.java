package ee.taltech.backoffice.game.repository;

import ee.taltech.backoffice.game.model.AnswerFrequency;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerFrequencyRepository extends CrudRepository<AnswerFrequency, Long> {

    @Query("SELECT * FROM kahoot.answer_frequency AS a WHERE a.room_id = :roomId AND a.question_id = :questionId")
    List<AnswerFrequency> findByRoomIdAndQuestionId(@Param("roomId") Long roomId, @Param("questionId") Long questionId);
}
