package ee.taltech.publicapplication.game.service.unit;

import ee.taltech.publicapplication.game.handler.answer_handler.AnswerHandler;
import ee.taltech.publicapplication.game.handler.black_list_handler.BlackListHandler;
import ee.taltech.publicapplication.game.handler.playerScores.ScoreHandler;
import ee.taltech.publicapplication.game.handler.player_handler.PlayerHandler;
import ee.taltech.publicapplication.game.handler.room.RoomHandler;
import ee.taltech.publicapplication.game.model.Room;
import ee.taltech.publicapplication.game.model.dto.register_room.RegisterRoomRequest;
import ee.taltech.publicapplication.game.security.service.TokenService;
import ee.taltech.publicapplication.game.service.AuthorService;
import ee.taltech.publicapplication.game.service.PlayedQuizService;
import ee.taltech.publicapplication.game.service.QuizService;
import ee.taltech.publicapplication.game.service.RoomService;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ee.taltech.publicapplication.game.model.RoomStatus.*;
import static ee.taltech.publicapplication.game.security.model.Authority.AUTHOR;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    AuthorService authorService;

    @Mock
    Clock clock;

    @Mock
    RoomService roomService;

    @Mock
    QuizService quizService;

    @Mock
    TokenService tokenService;

    @Mock
    BlackListHandler blackListHandler;

    @Mock
    RoomHandler roomHandler;

    @Mock
    ScoreHandler scoreHandler;

    @Mock
    PlayerHandler playerHandler;

    @Mock
    AnswerHandler answerHandler;

    @Mock
    PlayedQuizService playedQuizService;

    @BeforeEach
    void setUpAuthorServiceTest() {
        authorService = new AuthorService(
                clock,
                quizService,
                roomService,
                tokenService,
                blackListHandler,
                roomHandler,
                scoreHandler,
                answerHandler,
                playerHandler,
                playedQuizService
        );
    }

    @Test
    @Description("Should register a room")
    void shouldRegisterRoom() {
        Instant instant = Instant.now();

        when(clock.getZone()).thenReturn(ZoneId.of("Europe/Helsinki"));
        when(clock.instant()).thenReturn(instant);

        Room roomToSave = new Room()
                .setStartedAt(LocalDateTime.now(clock).toInstant(ZoneOffset.UTC))
                .setAuthorId(-10L)
                .setStatus(REGISTERED)
                .setQuizId(-88L)
                .setName("test room")
                .setCurrentQuestionId(null);
        when(roomService.save(roomToSave)).thenReturn(Mono.just(new Room().setId(-11L)));
        when(tokenService.generateJwt(-11L, -10L, List.of(AUTHOR))).thenReturn("test jwt");

        RegisterRoomRequest registerRoomRequest = new RegisterRoomRequest()
                .setRoomName("test room")
                .setQuizId(-88L);

        authorService.registerRoom(-10L, registerRoomRequest)
                .as(StepVerifier::create)
                .expectNext("test jwt")
                .verifyComplete();
    }

    @Test
    @Description("Should open a room")
    void shouldSetRoomOpen() {
        Room registeredRoom = new Room()
                .setAuthorId(-1L)
                .setId(-1L)
                .setStatus(REGISTERED);

        Room openedRoomWithPin = new Room()
                .setAuthorId(-1L)
                .setId(-1L)
                .setStatus(OPEN)
                .setPin("abcde");


        Set<String> allPins = Set.of("password");

        when(roomService.findById(-1L)).thenReturn(Mono.just(registeredRoom));
        when(roomService.setStatus(registeredRoom, OPEN)).thenCallRealMethod();
        when(roomHandler.getAllRoomPins()).thenReturn(allPins);
        when(roomService.generateRoomPin(allPins)).thenReturn("abcde");
        when(roomService.save(openedRoomWithPin)).thenReturn(Mono.just(openedRoomWithPin));
        when(blackListHandler.setup(openedRoomWithPin)).thenReturn(openedRoomWithPin);
        when(roomHandler.setup(openedRoomWithPin)).thenReturn(openedRoomWithPin);
        when(playerHandler.setup(openedRoomWithPin)).thenReturn(openedRoomWithPin);

        authorService
                .setRoomOpen(-1L)
                .as(StepVerifier::create)
                .expectNext(openedRoomWithPin)
                .verifyComplete();
    }

    @Test
    @Description("Should set a room to ready")
    void shouldSetRoomReady() {
        Room openedRoom = new Room()
                .setAuthorId(-2L)
                .setStatus(OPEN)
                .setId(-15L);

        Room readyRoom = new Room()
                .setAuthorId(-2L)
                .setStatus(READY)
                .setId(-15L);

        when(roomService.findById(-15L)).thenReturn(Mono.just(openedRoom));
        when(roomService.setStatus(openedRoom, READY)).thenCallRealMethod();
        when(roomService.save(readyRoom)).thenReturn(Mono.just(readyRoom));
        when(playerHandler.completeProcessor(readyRoom)).thenReturn(readyRoom);
        when(roomHandler.setRoomStatus(readyRoom, READY)).thenReturn(readyRoom);
        when(roomHandler.emit(readyRoom, READY)).thenReturn(readyRoom);

        authorService.setRoomReady(-15L)
                .as(StepVerifier::create)
                .expectNext(readyRoom)
                .verifyComplete();
    }

    @Test
    @Description("Should set room to answering")
    void shouldSetRoomAnswering() {
        Room readyRoom = new Room()
                .setId(-99L)
                .setAuthorId(-5L)
                .setStatus(READY);
        Room answeringRoom = new Room()
                .setId(-99L)
                .setAuthorId(-5L)
                .setStatus(ANSWERING);

        long currentTime = 1L;
        when(clock.millis()).thenReturn(currentTime);
        when(roomService.findById(-99L)).thenReturn(Mono.just(readyRoom));
        when(roomService.setStatus(readyRoom, ANSWERING)).thenCallRealMethod();
        when(roomHandler.setQuestionStartedAt(answeringRoom, currentTime)).thenReturn(answeringRoom);
        when(roomService.save(answeringRoom)).thenReturn(Mono.just(answeringRoom));
        when(roomHandler.setRoomStatus(answeringRoom, ANSWERING)).thenReturn(answeringRoom);
        when(roomHandler.emit(answeringRoom, ANSWERING)).thenReturn(answeringRoom);
        when(roomHandler.getPlayersCount(-99L)).thenReturn(0);

        authorService.setRoomAnswering(-99L)
                .as(StepVerifier::create)
                .expectNext(0)
                .verifyComplete();
    }

    @Test
    @Description("Should set room reviewing")
    void shouldSetRoomReviewing() {
        Room answeringRoom = new Room()
                .setAuthorId(-5L)
                .setId(-77L)
                .setStatus(ANSWERING);
        Room reviewingRoom = new Room()
                .setAuthorId(-5L)
                .setId(-77L)
                .setStatus(REVIEWING)
                .setCurrentQuestionId(-8L);

        when(roomService.findById(-77L)).thenReturn(Mono.just(answeringRoom));
        when(roomService.setStatus(answeringRoom, REVIEWING)).thenCallRealMethod();
        when(roomHandler.setNextQuestionId(-77L)).thenReturn(-8L);
        when(roomService.save(reviewingRoom)).thenReturn(Mono.just(reviewingRoom));
        when(scoreHandler.emit(reviewingRoom, true)).thenReturn(reviewingRoom);
        when(scoreHandler.clearRoomPlayersAnswered(reviewingRoom.setCurrentQuestionId(null))).thenReturn(reviewingRoom);
        when(roomHandler.emit(reviewingRoom, REVIEWING)).thenReturn(reviewingRoom);
        when(answerHandler.getAnswersStatistics(reviewingRoom.getId(), 0L)).thenReturn(Map.of());

        authorService.setRoomReviewing(-77L)
                .as(StepVerifier::create)
                .expectNext(Map.of())
                .verifyComplete();
    }

    @Test
    @Description("Should set room to Aborted")
    void shouldSetRoomAborted() {
        Room anyRoom = new Room()
                .setAuthorId(-89L)
                .setId(-15L)
                .setStatus(ANSWERING);
        Room abortedRoom = new Room()
                .setAuthorId(-89L)
                .setId(-15L)
                .setStatus(ABORTED);
        when(roomService.findById(-15L)).thenReturn(Mono.just(anyRoom));
        when(roomService.save(abortedRoom)).thenReturn(Mono.just(abortedRoom));
        when(roomHandler.emit(abortedRoom, ABORTED)).thenReturn(abortedRoom);

        when(playerHandler.clean(abortedRoom)).thenReturn(abortedRoom);
        when(roomHandler.clean(abortedRoom)).thenReturn(abortedRoom);
        when(blackListHandler.clean(abortedRoom)).thenReturn(abortedRoom);
        when(answerHandler.clean(abortedRoom)).thenReturn(abortedRoom);
        when(scoreHandler.clean(abortedRoom)).thenReturn(abortedRoom);

        authorService.setRoomAborted(-15L);
    }

    @Test
    @Description("Should kick a player")
    void shouldKickPlayer() {
        doNothing().when(roomHandler).removePlayer(-77L, -5L);
        doNothing().when(scoreHandler).removePlayerStatistics(-77L, -5L);
        doNothing().when(blackListHandler).addPlayer(-77L, -5L);

        authorService.removePlayer(-77L, -5L, "test username")
                .as(StepVerifier::create)
                .expectNext(-77L)
                .verifyComplete();
    }

}
