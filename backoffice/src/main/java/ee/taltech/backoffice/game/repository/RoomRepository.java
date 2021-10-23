package ee.taltech.backoffice.game.repository;

import ee.taltech.backoffice.game.model.Room;
import ee.taltech.backoffice.game.model.dto.RoomDto;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {

    @Query("SELECT * FROM kahoot.room AS r WHERE r.name = :name")
    Optional<Room> findByText(@Param("name") String name);

    @Query("SELECT * FROM kahoot.room AS r WHERE r.pin = :pin")
    Optional<Room> findByPin(@Param("pin") String pin);

    @Query("SELECT * FROM kahoot.room AS r WHERE r.quiz_id = :quiz_id")
    List<Room> findByQuizId(@Param("quiz_id") Long quizId);

    @Query(value = "SELECT kahoot.room.id AS id, kahoot.room.name AS room_name," +
            " kahoot.room.author_id AS author_id, " +
            "kahoot.quiz.name AS quiz_name, kahoot.room.played_quiz_id AS played_quiz_id," +
            "kahoot.room.started_at As started_at " +
            "FROM kahoot.room " +
            "INNER JOIN kahoot.quiz " +
            "ON kahoot.quiz.id = kahoot.room.quiz_id " +
            "WHERE kahoot.room.status = 'FINISHED' " +
            "AND kahoot.room.author_id = :author_id")
    List<RoomDto> findAuthorRooms(@Param("author_id") Long authorId);
}
