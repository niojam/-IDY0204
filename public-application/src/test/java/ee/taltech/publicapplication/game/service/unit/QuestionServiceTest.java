package ee.taltech.publicapplication.game.service.unit;

import ee.taltech.publicapplication.game.model.Question;
import ee.taltech.publicapplication.game.model.QuestionType;
import ee.taltech.publicapplication.game.model.TimeAlgorithm;
import ee.taltech.publicapplication.game.model.dto.AnswerDto;
import ee.taltech.publicapplication.game.model.dto.QuestionDto;
import ee.taltech.publicapplication.game.repository.QuestionRepository;
import ee.taltech.publicapplication.game.service.AnswerService;
import ee.taltech.publicapplication.game.service.QuestionService;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    QuestionRepository questionRepository;

    @Mock
    AnswerService answerService;

    QuestionService questionService;

    @BeforeEach
    void setQuestionRepositoryTest() {
        when(questionRepository
                .findAllByQuizIdOrderByIdDesc(-1L))
                .thenReturn(Flux.fromIterable(List.of(
                        new Question()
                                .setId(-1L)
                                .setQuestionType(QuestionType.SINGLE_MATCH)
                                .setTimeAlgorithm(TimeAlgorithm.FASTEST_ANSWER)
                                .setQuizId(-1L),
                        new Question()
                                .setId(-2L)
                                .setQuestionType(QuestionType.MULTIPLE_ANY)
                                .setTimeAlgorithm(TimeAlgorithm.CONSTANT)
                                .setQuizId(-1L))));

        when(answerService.findAnswersForQuestion(-1L))
                .thenReturn(Flux.fromIterable(List.of(
                        new AnswerDto()
                                .setId(-1L)
                                .setQuestionId(-1L)
                                .setText("First answer"),
                        new AnswerDto()
                                .setId(-2L)
                                .setQuestionId(-1L)
                                .setText("Second answer"))));

        when(answerService.findAnswersForQuestion(-2L))
                .thenReturn(Flux.fromIterable(List.of(
                        new AnswerDto()
                                .setId(-3L)
                                .setQuestionId(-1L)
                                .setText("Fourth answer"),
                        new AnswerDto()
                                .setId(-4L)
                                .setQuestionId(-1L)
                                .setText("Fifth answer"))));
        questionService = new QuestionService(answerService, questionRepository);
    }

    @Test
    @Description("Should compose question dtos")
    void shouldComposeQuestionDtos() {
        questionService
                .findByQuizId(-1L)
                .as(StepVerifier::create)
                .expectNext(
                        new QuestionDto()
                                .setId(-1L)
                                .setQuizId(-1L)
                                .setQuestionType(QuestionType.SINGLE_MATCH)
                                .setTimeAlgorithm(TimeAlgorithm.FASTEST_ANSWER)
                                .setAnswers(List.of(
                                        new AnswerDto()
                                                .setId(-1L)
                                                .setQuestionId(-1L)
                                                .setText("First answer"),
                                        new AnswerDto()
                                                .setId(-2L)
                                                .setQuestionId(-1L)
                                                .setText("Second answer"))),
                        new QuestionDto()
                                .setId(-2L)
                                .setQuizId(-1L)
                                .setQuestionType(QuestionType.MULTIPLE_ANY)
                                .setTimeAlgorithm(TimeAlgorithm.CONSTANT)
                                .setAnswers(List.of(
                                        new AnswerDto()
                                                .setId(-3L)
                                                .setQuestionId(-1L)
                                                .setText("Fourth answer"),
                                        new AnswerDto()
                                                .setId(-4L)
                                                .setQuestionId(-1L)
                                                .setText("Fifth answer"))))
                .verifyComplete();
    }

}