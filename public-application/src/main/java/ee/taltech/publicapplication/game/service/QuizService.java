package ee.taltech.publicapplication.game.service;

import ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest;
import ee.taltech.publicapplication.game.model.dto.QuizDto;
import ee.taltech.publicapplication.game.model.mapper.QuizMapper;
import ee.taltech.publicapplication.game.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest.Code.BUSINESS_LOGIC_EXCEPTION;
import static java.lang.String.format;

@Service
@Transactional
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionService questionService;
    private final QuizMapper quizMapper = Mappers.getMapper(QuizMapper.class);

    public Mono<QuizDto> findById(Long id) {
        return quizRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new BadRequest(
                        BUSINESS_LOGIC_EXCEPTION,
                        format("No quiz found with id=%s", id))))
                .map(quizMapper::toDto)
                .flatMap(quizDto -> fillQuestions(quizDto, id));
    }

    private Mono<QuizDto> fillQuestions(QuizDto quizDto, Long quizId) {
        return questionService
                .findByQuizId(quizId)
                .buffer()
                .map(quizDto::setQuestions)
                .then(Mono.just(quizDto));
    }

}
