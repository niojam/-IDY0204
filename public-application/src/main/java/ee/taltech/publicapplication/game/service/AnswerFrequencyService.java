package ee.taltech.publicapplication.game.service;

import ee.taltech.publicapplication.game.model.AnswerFrequency;
import ee.taltech.publicapplication.game.repository.AnswerFrequencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.Collection;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerFrequencyService {

    private final AnswerFrequencyRepository repository;

    public Flux<AnswerFrequency> saveAll(Collection<AnswerFrequency> frequencies) {
        return repository.saveAll(frequencies);
    }

}
