package ee.taltech.publicapplication.game.repository;

import ee.taltech.publicapplication.game.model.Player;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends ReactiveCrudRepository<Player, Long> {
}
