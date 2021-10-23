package ee.taltech.backoffice.game.repository;

import ee.taltech.backoffice.game.model.Player;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

    @Query("SELECT * FROM kahoot.player AS p WHERE p.username = :username")
    Optional<Player> findByUsername(@Param("username") String username);


    @NotNull
    @Query("SELECT * FROM kahoot.player")
    List<Player> findAll();

    @Query("SELECT * FROM kahoot.player AS p WHERE p.room_id = :roomId")
    List<Player> findByRoomId(@Param("roomId") Long roomId);
}
