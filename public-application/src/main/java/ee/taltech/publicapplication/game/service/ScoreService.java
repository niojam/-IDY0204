package ee.taltech.publicapplication.game.service;

import ee.taltech.publicapplication.game.model.Score;
import ee.taltech.publicapplication.game.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.Collection;

@Service
@Transactional
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;

    public Flux<Score> saveAll(Collection<Score> scores) {
        return scoreRepository.saveAll(scores);
    }

}
