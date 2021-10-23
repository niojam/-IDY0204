package ee.taltech.publicapplication.game.service.unit;

import ee.taltech.publicapplication.game.model.QuestionType;
import ee.taltech.publicapplication.game.model.Quiz;
import ee.taltech.publicapplication.game.model.TimeAlgorithm;
import ee.taltech.publicapplication.game.model.dto.AnswerDto;
import ee.taltech.publicapplication.game.model.dto.QuestionDto;
import ee.taltech.publicapplication.game.model.dto.QuizDto;
import ee.taltech.publicapplication.game.repository.QuizRepository;
import ee.taltech.publicapplication.game.service.QuestionService;
import ee.taltech.publicapplication.game.service.QuizService;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    QuizService quizService;

    @Mock
    QuizRepository quizRepository;

    @Mock
    QuestionService questionService;


    @BeforeEach
    void setQuizServiceTest() {
        when(quizRepository.findById(-1L))
                .thenReturn(Mono.just(
                        new Quiz()
                                .setAuthorId(-2L)
                                .setName("Super quiz")
                ));
        when(questionService.findByQuizId(-1L))
                .thenReturn(Flux.fromIterable(List.of(

                        new QuestionDto()
                                .setId(-1L)
                                .setQuizId(-1L)
                                .setTimeAlgorithm(TimeAlgorithm.FASTEST_ANSWER)
                                .setQuestionType(QuestionType.MULTIPLE_ANY)
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
                                .setTimeAlgorithm(TimeAlgorithm.FASTEST_ANSWER)
                                .setQuestionType(QuestionType.MULTIPLE_ANY)
                                .setAnswers(List.of(
                                        new AnswerDto()
                                                .setId(-3L)
                                                .setQuestionId(-1L)
                                                .setText("Fourth answer"),
                                        new AnswerDto()
                                                .setId(-4L)
                                                .setQuestionId(-1L)
                                                .setText("Fifth answer")))

                )));
        quizService = new QuizService(quizRepository, questionService);
    }

    @Test
    @Description("Should compose quiz dto")
    void shouldComposeQuizDto() {
        quizService
                .findById(-1L)
                .as(StepVerifier::create)
                .expectNext(
                        new QuizDto()
                                .setAuthorId(-2L)
                                .setName("Super quiz")
                                .setQuestions(List.of(
                                                new QuestionDto()
                                                        .setId(-1L)
                                                        .setQuizId(-1L)
                                                        .setTimeAlgorithm(TimeAlgorithm.FASTEST_ANSWER)
                                                        .setQuestionType(QuestionType.MULTIPLE_ANY)
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
                                                        .setTimeAlgorithm(TimeAlgorithm.FASTEST_ANSWER)
                                                        .setQuestionType(QuestionType.MULTIPLE_ANY)
                                                        .setAnswers(List.of(
                                                                new AnswerDto()
                                                                        .setId(-3L)
                                                                        .setQuestionId(-1L)
                                                                        .setText("Fourth answer"),
                                                                new AnswerDto()
                                                                        .setId(-4L)
                                                                        .setQuestionId(-1L)
                                                                        .setText("Fifth answer"))))))
                .verifyComplete();
    }

}