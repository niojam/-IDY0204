package ee.taltech.publicapplication.game.service.integration;

import ee.taltech.publicapplication.game.abstract_test.AbstractTestWithRepository;
import ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest;
import ee.taltech.publicapplication.game.handler.answer_handler.AnswerHandler;
import ee.taltech.publicapplication.game.handler.black_list_handler.BlackListHandler;
import ee.taltech.publicapplication.game.handler.event_handler.PlayerWatcher;
import ee.taltech.publicapplication.game.handler.event_handler.RoomStatusUpdatedWatcher;
import ee.taltech.publicapplication.game.handler.event_handler.ScoreEmitterMap;
import ee.taltech.publicapplication.game.handler.playerScores.ScoreHandler;
import ee.taltech.publicapplication.game.handler.player_handler.PlayerHandler;
import ee.taltech.publicapplication.game.handler.room.RoomHandler;
import ee.taltech.publicapplication.game.model.Room;
import ee.taltech.publicapplication.game.model.dto.PlayerDto;
import ee.taltech.publicapplication.game.model.dto.give_answer.GiveAnswerRequest;
import ee.taltech.publicapplication.game.model.dto.register_room.RegisterRoomRequest;
import ee.taltech.publicapplication.game.security.service.TokenService;
import ee.taltech.publicapplication.game.security.user_details.PublicAppUserDetails;
import ee.taltech.publicapplication.game.service.AuthorService;
import ee.taltech.publicapplication.game.service.PlayerService;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;

import static ee.taltech.publicapplication.game.security.model.Authority.AUTHOR;
import static ee.taltech.publicapplication.game.security.model.Authority.PLAYER;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class FlowTest extends AbstractTestWithRepository {

    @Autowired
    AuthorService authorService;

    @Autowired
    PlayerService playerService;

    @MockBean
    TokenService tokenService;

    @Captor
    ArgumentCaptor<Long> roomIdArgumentCaptor;

    @Captor
    ArgumentCaptor<Long> playerIdArgumentCaptor;

    @Autowired
    RoomHandler roomHandler;

    @Autowired
    AnswerHandler answerHandler;

    @Autowired
    BlackListHandler blackListHandler;

    @Autowired
    ScoreHandler scoreHandler;

    @Autowired
    PlayerHandler playerHandler;

    @Autowired
    PlayerWatcher playerWatcher;

    @Autowired
    RoomStatusUpdatedWatcher roomStatusUpdatedWatcher;

    @Autowired
    ScoreEmitterMap scoreEmitterMap;

    @Test
    @Description("Should clear everything related to room on FINISHED")
    void shouldClearOnFinished() {
        when(tokenService.generateJwt(roomIdArgumentCaptor.capture(), eq(-1L), eq(List.of(AUTHOR)))).thenReturn("token");
        // create and set REGISTERED
        RegisterRoomRequest registerRoomRequest = new RegisterRoomRequest()
                .setRoomName("Super Room")
                .setQuizId(-1L);

        authorService.registerRoom(-1L, registerRoomRequest)
                .as(StepVerifier::create)
                .assertNext(token -> assertThat(token).isNotBlank())
                .verifyComplete();

        Long roomId = roomIdArgumentCaptor.getValue();
        when(tokenService.generateJwt(eq(roomId), anyLong(), eq(List.of(PLAYER)))).thenReturn("token");

        // set OPEN
        authorService.setRoomOpen(roomId)
                .map(Room::getPin)
                // FIRST player authorizes
                .flatMap(playerService::accessRoom)
                .subscribe();

        // AUTHOR fetches quiz
        authorService.getQuiz(roomId)
                .as(StepVerifier::create)
                .assertNext(next -> {
                })
                .verifyComplete();

        verify(tokenService, timeout(2000)).generateJwt(eq(roomId), playerIdArgumentCaptor.capture(), eq(List.of(PLAYER)));
        Long playerId = playerIdArgumentCaptor.getValue();

        // FIRST joins room
        playerService.joinRoom(roomId, playerId, "super-player")
                .as(StepVerifier::create)
                .assertNext(next -> assertThat(next).isEqualTo(new PlayerDto()
                        .setPlayerId(playerId)
                        .setUsername("super-player")))
                .verifyComplete();

        // FIRST starts listening for status changes
        playerService.getFluxRoomStatus(roomId).subscribe();

        // start listening to score changes
        playerService.getScore(roomId, playerId).subscribe();

        // FIRST gets simplified quiz
        playerService.getSimplifiedQuiz(roomId)
                .as(StepVerifier::create)
                .assertNext(next -> {
                })
                .verifyComplete();

        // set READY
        authorService.setRoomReady(roomId)
                .as(StepVerifier::create)
                .assertNext(next -> {
                })
                .verifyComplete();

        // set room ANSWERING
        authorService.setRoomAnswering(roomId)
                .as(StepVerifier::create)
                .assertNext(next -> {
                })
                .verifyComplete();

        // FIRST gives his answer to a first question
        playerService.saveAnswer(new PublicAppUserDetails(roomId, playerId, List.of()),
                new GiveAnswerRequest()
                        .setAnswerIds(Set.of(-1L))
                        .setQuestionId(-1L))
                .as(StepVerifier::create)
                .assertNext(next -> {
                })
                .verifyComplete();

        // set room REVIEWING
        authorService.setRoomReviewing(roomId)
                .as(StepVerifier::create)
                .assertNext(next -> {
                })
                .verifyComplete();

        // set room ANSWERING
        authorService.setRoomAnswering(roomId)
                .as(StepVerifier::create)
                .assertNext(next -> {
                })
                .verifyComplete();

        // set room REVIEWING
        authorService.setRoomReviewing(roomId)
                .as(StepVerifier::create)
                .assertNext(next -> {
                })
                .verifyComplete();

        // set room FINISHED
        authorService.setRoomFinished(roomId)
                .as(StepVerifier::create)
                .assertNext(next -> {
                })
                .verifyComplete();

        // check that everything is cleared on FINISHED
        assertThatThrownBy(() -> roomHandler.getRoomStatus(roomId))
                .isInstanceOf(BadRequest.class)
                .hasMessage(format("No room found with roomId=%d", roomId));
        assertThat(roomHandler.getAllRoomPins().size()).isEqualTo(0);
        assertThatThrownBy(() -> playerHandler.isUsernameNotAvailable(roomId, "abc"))
                .isInstanceOf(BadRequest.class)
                .hasMessage(format("No usernames found for roomId=%d", roomId));
        assertThat(playerWatcher.contains(roomId)).isFalse();
        assertThat(roomStatusUpdatedWatcher.contains(roomId)).isFalse();

        assertThatThrownBy(() -> answerHandler.getAnswersStatistics(roomId, -1L))
                .isInstanceOf(BadRequest.class)
                .hasMessage(format("No answers found for roomId=%d", roomId));

        assertThatThrownBy(() -> blackListHandler.containsPlayer(roomId, -1L))
                .isInstanceOf(BadRequest.class)
                .hasMessage(format("No black-list for with roomId=%d found", roomId));

        assertThatThrownBy(() -> scoreHandler.getScore(roomId, -1L))
                .isInstanceOf(BadRequest.class)
                .hasMessage(format("No score found for roomId=%d", roomId));
//        assertThatThrownBy(() -> scoreHandler.clearRoomPlayersAnswered(roomId))
//                .isInstanceOf(BadRequest.class)
//                .hasMessage(format("No playersAnswered found for roomId=%d", roomId));
        assertThat(scoreEmitterMap.contains(roomId)).isFalse();

    }

    @Test
    @Description("Should clear everything related to room on FINISHED")
    void shouldClearOnAborted() {
        when(tokenService.generateJwt(roomIdArgumentCaptor.capture(), eq(-1L), eq(List.of(AUTHOR)))).thenReturn("token");
        // create and set REGISTERED
        RegisterRoomRequest registerRoomRequest = new RegisterRoomRequest()
                .setRoomName("Super Room")
                .setQuizId(-1L);

        authorService.registerRoom(-1L, registerRoomRequest)
                .as(StepVerifier::create)
                .assertNext(token -> assertThat(token).isNotBlank())
                .verifyComplete();

        Long roomId = roomIdArgumentCaptor.getValue();
        when(tokenService.generateJwt(eq(roomId), anyLong(), eq(List.of(PLAYER)))).thenReturn("token");

        // set OPEN
        authorService.setRoomOpen(roomId)
                .map(Room::getPin)
                // FIRST player authorizes
                .flatMap(playerService::accessRoom)
                .subscribe();

        // AUTHOR fetches quiz
        authorService.getQuiz(roomId)
                .as(StepVerifier::create)
                .assertNext(next -> {
                })
                .verifyComplete();

        verify(tokenService, timeout(2000)).generateJwt(eq(roomId), playerIdArgumentCaptor.capture(), eq(List.of(PLAYER)));
        Long playerId = playerIdArgumentCaptor.getValue();

        // FIRST joins room
        playerService.joinRoom(roomId, playerId, "super-player")
                .as(StepVerifier::create)
                .assertNext(next -> assertThat(next).isEqualTo(new PlayerDto()
                        .setPlayerId(playerId)
                        .setUsername("super-player")))
                .verifyComplete();

        // FIRST starts listening for status changes
        playerService.getFluxRoomStatus(roomId).subscribe();

        // start listening to score changes
        playerService.getScore(roomId, playerId).subscribe();

        // FIRST gets simplified quiz
        playerService.getSimplifiedQuiz(roomId)
                .as(StepVerifier::create)
                .assertNext(next -> {
                })
                .verifyComplete();

        // set READY
        authorService.setRoomReady(roomId)
                .as(StepVerifier::create)
                .assertNext(next -> {
                })
                .verifyComplete();

        // set room ANSWERING
        authorService.setRoomAnswering(roomId)
                .as(StepVerifier::create)
                .assertNext(next -> {
                })
                .verifyComplete();

        // FIRST gives his answer to a first question
        playerService.saveAnswer(new PublicAppUserDetails(roomId, playerId, List.of()),
                new GiveAnswerRequest()
                        .setAnswerIds(Set.of(-1L))
                        .setQuestionId(-1L))
                .as(StepVerifier::create)
                .assertNext(next -> {
                })
                .verifyComplete();

        // set room REVIEWING
        authorService.setRoomReviewing(roomId)
                .as(StepVerifier::create)
                .assertNext(next -> {
                })
                .verifyComplete();

        // set room ANSWERING
        authorService.setRoomAnswering(roomId)
                .as(StepVerifier::create)
                .assertNext(next -> {
                })
                .verifyComplete();

        // set room REVIEWING
        authorService.setRoomReviewing(roomId)
                .as(StepVerifier::create)
                .assertNext(next -> {
                })
                .verifyComplete();

        // set room ABORTED
        authorService.setRoomAbortedReactive(roomId)
                .as(StepVerifier::create)
                .assertNext(next -> {
                })
                .verifyComplete();

        // check that everything is cleared on FINISHED
        assertThatThrownBy(() -> roomHandler.getRoomStatus(roomId))
                .isInstanceOf(BadRequest.class)
                .hasMessage(format("No room found with roomId=%d", roomId));
        assertThat(roomHandler.getAllRoomPins().size()).isEqualTo(0);
        assertThatThrownBy(() -> playerHandler.isUsernameNotAvailable(roomId, "abc"))
                .isInstanceOf(BadRequest.class)
                .hasMessage(format("No usernames found for roomId=%d", roomId));
        assertThat(playerWatcher.contains(roomId)).isFalse();
        assertThat(roomStatusUpdatedWatcher.contains(roomId)).isFalse();

        assertThatThrownBy(() -> answerHandler.getAnswersStatistics(roomId, -1L))
                .isInstanceOf(BadRequest.class)
                .hasMessage(format("No answers found for roomId=%d", roomId));

        assertThatThrownBy(() -> blackListHandler.containsPlayer(roomId, -1L))
                .isInstanceOf(BadRequest.class)
                .hasMessage(format("No black-list for with roomId=%d found", roomId));

        assertThatThrownBy(() -> scoreHandler.getScore(roomId, -1L))
                .isInstanceOf(BadRequest.class)
                .hasMessage(format("No score found for roomId=%d", roomId));
//        assertThatThrownBy(() -> scoreHandler.clearRoomPlayersAnswered(roomId))
//                .isInstanceOf(BadRequest.class)
//                .hasMessage(format("No playersAnswered found for roomId=%d", roomId));
        assertThat(scoreEmitterMap.contains(roomId)).isFalse();
    }

}
