package ee.taltech.publicapplication.game.repository;

import ee.taltech.publicapplication.game.model.Score;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends ReactiveCrudRepository<Score, Long> {
}
