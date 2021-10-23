package ee.taltech.publicapplication.game.service.unit;

import ee.taltech.publicapplication.game.handler.event_handler.RoomStatusUpdatedWatcher;
import ee.taltech.publicapplication.game.handler.room.RoomHandler;
import ee.taltech.publicapplication.game.model.Room;
import ee.taltech.publicapplication.game.model.dto.AnswerDto;
import ee.taltech.publicapplication.game.model.dto.QuestionDto;
import ee.taltech.publicapplication.game.model.dto.QuizDto;
import ee.taltech.publicapplication.game.service.GameService;
import ee.taltech.publicapplication.game.service.QuizService;
import ee.taltech.publicapplication.game.service.RoomService;
import ee.taltech.publicapplication.game.utils.TestUtils;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    GameService gameService;

    @Mock
    RoomService roomService;

    @Mock
    QuizService quizService;

    @Mock
    RoomStatusUpdatedWatcher roomStatusUpdatedWatcher;

    RoomHandler roomHandler;

    @BeforeEach
    void setGameServiceTest() {
        roomHandler = new RoomHandler(roomStatusUpdatedWatcher);
        when(roomService.findById(-1L))
                .thenReturn(Mono.just(
                        new Room()
                                .setQuizId(-1L)));
        when(quizService.findById(-1L))
                .thenReturn(Mono.just(
                        new QuizDto()
                                .setName("Super quiz")
                                .setQuestions(List.of(
                                        new QuestionDto()
                                                .setId(-1L)
                                                .setQuizId(-1L)
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
                                                .setAnswers(List.of(
                                                        new AnswerDto()
                                                                .setId(-3L)
                                                                .setQuestionId(-1L)
                                                                .setText("Fourth answer"),
                                                        new AnswerDto()
                                                                .setId(-4L)
                                                                .setQuestionId(-1L)
                                                                .setText("Fifth answer")))))));
        gameService = new GameService(roomService, quizService, roomHandler);
    }

    @Test
    @Description("Should find room and fetch its quiz in dto")
    void shouldFindRoomAndFetchQuiz() {
        gameService
                .getQuiz(TestUtils.playerPublicAppUserDetails(-1L))
                .as(StepVerifier::create)
                .expectNext(
                        new QuizDto()
                                .setName("Super quiz")
                                .setQuestions(List.of(
                                        new QuestionDto()
                                                .setId(-1L)
                                                .setQuizId(-1L)
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