package ee.taltech.backoffice.game.repository;

import ee.taltech.backoffice.game.model.Score;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScoreRepository extends CrudRepository<Score, Long> {

    @Query("SELECT * FROM kahoot.score AS s WHERE s.room_id = :roomId AND s.player_id = :playerId")
    Optional<Score> findByRoomIdAndPlayerId(@Param("roomId") Long roomId, @Param("playerId") Long playerId);

}
