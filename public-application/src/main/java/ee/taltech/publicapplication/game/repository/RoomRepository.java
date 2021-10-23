package ee.taltech.publicapplication.game.repository;

import ee.taltech.publicapplication.game.model.Room;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RoomRepository extends ReactiveCrudRepository<Room, Long> {

    Mono<Room> findRoomByPin(String pin);

    @Query("SELECT r.id, r.status FROM kahoot.room as r WHERE r.id in :ids")
    Flux<Room> findAllByIds(@Param("ids") Iterable<Long> ids);

}
