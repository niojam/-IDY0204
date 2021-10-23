package ee.taltech.publicapplication.game.service.integration;

import ee.taltech.publicapplication.game.abstract_test.AbstractTestWithRepository;
import ee.taltech.publicapplication.game.handler.answer_handler.AnswerHandler;
import ee.taltech.publicapplication.game.handler.black_list_handler.BlackListHandler;
import ee.taltech.publicapplication.game.handler.playerScores.ScoreHandler;
import ee.taltech.publicapplication.game.handler.player_handler.PlayerHandler;
import ee.taltech.publicapplication.game.handler.room.RoomHandler;
import ee.taltech.publicapplication.game.model.Room;
import ee.taltech.publicapplication.game.model.dto.AnswerDto;
import ee.taltech.publicapplication.game.model.dto.PlayerDto;
import ee.taltech.publicapplication.game.model.dto.QuestionDto;
import ee.taltech.publicapplication.game.model.dto.QuizDto;
import ee.taltech.publicapplication.game.model.dto.register_room.RegisterRoomRequest;
import ee.taltech.publicapplication.game.security.service.TokenService;
import ee.taltech.publicapplication.game.service.AuthorService;
import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.test.StepVerifier;

import java.util.List;

import static ee.taltech.publicapplication.game.model.RoomStatus.*;
import static ee.taltech.publicapplication.game.security.model.Authority.AUTHOR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthorServiceIntegrationTest extends AbstractTestWithRepository {

    @Autowired
    AuthorService authorService;

    @Autowired
    RoomHandler roomHandler;

    @Autowired
    ScoreHandler scoreHandler;

    @Autowired
    AnswerHandler answerHandler;

    @Autowired
    BlackListHandler blackListHandler;

    @Autowired
    PlayerHandler playerHandler;

    @MockBean
    TokenService tokenService;

    @Test
    @Description("Should kick player")
    void shouldKickPlayer() {
        // add room
        Room roomToAdd = new Room()
                .setAuthorId(-1L)
                .setId(-1L)
                .setPin("stronk password");
        roomHandler.setup(roomToAdd);

        scoreHandler.setup(-1L);
        scoreHandler.createPlayerScore(-1L, -1L, "player1");

        blackListHandler.setup(roomToAdd);
        playerHandler.setup(roomToAdd);

        // add player that will be kicked in a subsequent request
        PlayerDto playerDto = roomHandler.addPlayer(-1L, -2L, "player1").block();
        if (playerDto != null) {
            playerHandler.addPlayer(-1L, playerDto);
        }

        // username is not available because there is a player with the same username
        Boolean isNotAvailable = playerHandler.isUsernameNotAvailable(-1L, "player1");
        assertThat(isNotAvailable).isTrue();

        // player is not in black-list
        Boolean isKicked = blackListHandler.containsPlayer(-1L, -2L);
        assertThat(isKicked).isFalse();

        // kick player
        authorService.removePlayer(-1L, -2L, "player1")
                .as(StepVerifier::create)
                .assertNext(next -> assertThat(next).isEqualTo(-1L))
                .verifyComplete();

        // username is available
        isNotAvailable = playerHandler.isUsernameNotAvailable(-1L, "player1");
        assertThat(isNotAvailable).isFalse();

        // player is added to black-list
        isKicked = blackListHandler.containsPlayer(-1L, -2L);
        assertThat(isKicked).isTrue();
    }

    @Test
    @Description("Should register room")
    void shouldRegisterRoom() {
        RegisterRoomRequest registerRoomRequest = new RegisterRoomRequest()
                .setQuizId(-3L)
                .setRoomName("cool name");
        when(tokenService.generateJwt(anyLong(), eq(-3L), eq(List.of(AUTHOR)))).thenReturn("test jwt");

        authorService.registerRoom(-3L, registerRoomRequest)
                .as(StepVerifier::create)
                .expectNext("test jwt")
                .verifyComplete();
    }

    @Test
    @Description("Should set room to OPEN and generate pin")
    void shouldSetRoomToOpen() {
        authorService.setRoomOpen(-3L)
                .as(StepVerifier::create)
                .assertNext(next -> {
                    assertThat(next.getStatus()).isEqualTo(OPEN);

                    assertThat(blackListHandler.containsPlayer(-3L, -1L)).isFalse();

                    assertThat(roomHandler.getRoomId(next.getPin())).isEqualTo(-3L);
                    assertThat(roomHandler.getAllRoomPins().contains(next.getPin())).isTrue();
                    assertThat(roomHandler.getEvents(-3L)).isNotNull();
                    assertThat(roomHandler.getProcessor(-3L)).isNotNull();

                    assertThat(playerHandler.isUsernameNotAvailable(-3L, "any")).isFalse();
                })
                .verifyComplete();
    }

    @Test
    @Description("Should set room to ready")
    void shouldSetRoomToReady() {
        Room registeredRoom = new Room()
                .setStatus(REGISTERED)
                .setId(-4L)
                .setAuthorId(-3L)
                .setCurrentQuestionId(-2L)
                .setName("room -4 name")
                .setPin("4984984")
                .setQuizId(-3L);
        roomHandler.setup(registeredRoom);
        blackListHandler.setup(registeredRoom);

        authorService.setRoomReady(-4L)
                .as(StepVerifier::create)
                .assertNext(next -> {
                    assertThat(next.getStatus()).isEqualTo(READY);

                    roomHandler.getProcessor(-4L)
                            .as(StepVerifier::create)
                            .assertNext(next2 -> assertThat(next2).isEqualTo(READY));
                })
                .verifyComplete();
    }

    @Test
    @Description("Should set room to ANSWERING")
    void shouldSetRoomAnswering() {
        Room readyRoom = new Room()
                .setStatus(READY)
                .setId(-5L)
                .setAuthorId(-3L)
                .setCurrentQuestionId(-1L)
                .setName("room -4 name")
                .setPin("4984984")
                .setQuizId(-3L);

        roomHandler.setup(readyRoom);
        roomHandler.addQuiz(readyRoom.getId(), new QuizDto()
                .setName("Super quiz")
                .setFirstQuestionId(-1L)
                .setQuestions(List.of(
                        new QuestionDto()
                                .setId(-1L)
                                .setQuizId(-1L)
                                .setAnswers(List.of(
                                        new AnswerDto()
                                                .setId(-1L)
                                                .setQuestionId(-1L)
                                                .setIsCorrect(false)
                                                .setText("First answer"),
                                        new AnswerDto()
                                                .setId(-2L)
                                                .setIsCorrect(true)
                                                .setQuestionId(-1L)
                                                .setText("Second answer"))),
                        new QuestionDto()
                                .setId(-2L)
                                .setQuizId(-1L)
                                .setAnswers(List.of(
                                        new AnswerDto()
                                                .setId(-3L)
                                                .setIsCorrect(true)
                                                .setQuestionId(-1L)
                                                .setText("Fourth answer"),
                                        new AnswerDto()
                                                .setId(-4L)
                                                .setQuestionId(-1L)
                                                .setIsCorrect(false)
                                                .setText("Fifth answer"))))));
        scoreHandler.setup(-5L);

        authorService.setRoomAnswering(-5L)
                .as(StepVerifier::create)
                .assertNext(next -> {
                    assertThat(next).isEqualTo(0);

                    roomHandler.getProcessor(-5L)
                            .as(StepVerifier::create)
                            .assertNext(next2 -> assertThat(next2).isEqualTo(ANSWERING));
                })
                .verifyComplete();
    }

    @Test
    @Description("Should set room to REVIEWING")
    void shouldSetRoomReviewing() {
        Room answeringRoom = new Room()
                .setStatus(ANSWERING)
                .setId(-6L)
                .setAuthorId(-3L)
                .setCurrentQuestionId(-2L)
                .setName("room -4 name")
                .setPin("4984984")
                .setQuizId(-3L);

        QuizDto quiz = new QuizDto()
                .setFirstQuestionId(-1L)
                .setName("test quiz")
                .setQuestions(List.of(new QuestionDto()
                        .setId(-1L)
                        .setNextQuestionId(-2L)
                        .setReward(100L)
                        .setAnswers(List.of(new AnswerDto()
                                .setQuestionId(-1L)
                                .setId(-1L)
                                .setIsCorrect(true)))));
        roomHandler.setup(answeringRoom);
        roomHandler.addQuiz(-6L, quiz);
        scoreHandler.setup(-6L);
        answerHandler.setup(-6L, quiz);

        authorService.setRoomReviewing(-6L)
                .as(StepVerifier::create)
                .assertNext(next -> {
                    assertThat(next.size()).isEqualTo(1);

                    assertThat(roomHandler.getCurrentQuestionId(-6L)).isEqualTo(-2L);
                })
                .verifyComplete();
    }

    @Test
    @Description("Should set room to ABORTED")
    void shouldSetRoomAborted() {
        Room answeringRoom = new Room()
                .setStatus(ANSWERING)
                .setId(-7L)
                .setAuthorId(-3L)
                .setCurrentQuestionId(-2L)
                .setName("room -4 name")
                .setPin("4984984")
                .setQuizId(-3L);

        roomHandler.setup(answeringRoom);
        blackListHandler.setup(answeringRoom);
        scoreHandler.setup(-7L);
        answerHandler.setup(-7L, new QuizDto()
                .setFirstQuestionId(-1L)
                .setName("test quiz")
                .setQuestions(List.of(new QuestionDto()
                        .setAnswers(List.of(new AnswerDto()
                                .setId(-1L)
                                .setIsCorrect(true))))));

        Assertions.assertDoesNotThrow(() -> authorService.setRoomAborted(-7L));
    }

}
