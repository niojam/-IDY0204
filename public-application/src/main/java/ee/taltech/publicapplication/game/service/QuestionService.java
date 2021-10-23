package ee.taltech.publicapplication.game.service;

import ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest;
import ee.taltech.publicapplication.game.model.dto.QuestionDto;
import ee.taltech.publicapplication.game.model.mapper.QuestionMapper;
import ee.taltech.publicapplication.game.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import static ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest.Code.BUSINESS_LOGIC_EXCEPTION;
import static java.lang.String.format;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {

    private final AnswerService answerService;
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper = Mappers.getMapper(QuestionMapper.class);

    public Flux<QuestionDto> findByQuizId(Long quizId) {
        return questionRepository
                .findAllByQuizIdOrderByIdDesc(quizId)
                .switchIfEmpty(Flux.error(new BadRequest(
                        BUSINESS_LOGIC_EXCEPTION,
                        format("No questions found for quiz with id=%d", quizId))))
                .map(questionMapper::toDto)
                .flatMap(this::setAnswers);
    }

    private Flux<QuestionDto> setAnswers(QuestionDto questionDto) {
        return answerService
                .findAnswersForQuestion(questionDto.getId())
                .buffer()
                .map(questionDto::setAnswers);
    }

}
