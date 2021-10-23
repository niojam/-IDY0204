package ee.taltech.publicapplication.game.service;

import ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest;
import ee.taltech.publicapplication.game.handler.black_list_handler.BlackListHandler;
import ee.taltech.publicapplication.game.handler.player_handler.PlayerHandler;
import ee.taltech.publicapplication.game.handler.room.RoomHandler;
import ee.taltech.publicapplication.game.model.Player;
import ee.taltech.publicapplication.game.model.RoomStatus;
import ee.taltech.publicapplication.game.model.dto.PlayerDto;
import ee.taltech.publicapplication.game.model.dto.QuizDto;
import ee.taltech.publicapplication.game.model.dto.ScoreDto;
import ee.taltech.publicapplication.game.model.dto.common.GeneralResponse;
import ee.taltech.publicapplication.game.model.dto.give_answer.GiveAnswerRequest;
import ee.taltech.publicapplication.game.model.dto.room_status.RoomStatusResponse;
import ee.taltech.publicapplication.game.model.dto.watch_players.WatchPlayerResponse;
import ee.taltech.publicapplication.game.repository.PlayerRepository;
import ee.taltech.publicapplication.game.security.service.TokenService;
import ee.taltech.publicapplication.game.security.user_details.PublicAppUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest.Code.BUSINESS_LOGIC_EXCEPTION;
import static ee.taltech.publicapplication.game.model.RoomStatus.ANSWERING;
import static ee.taltech.publicapplication.game.model.RoomStatus.OPEN;
import static ee.taltech.publicapplication.game.security.model.Authority.PLAYER;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final RoomHandler roomHandler;
    private final BlackListHandler blackListHandler;
    private final PlayerHandler playerHandler;

    private final TokenService tokenService;
    private final AnswerService answerService;
    private final AuthorService authorService;

    @Transactional
    public Mono<String> accessRoom(String roomPin) {
        return Mono.just(roomHandler.getRoomId(roomPin))
                .flatMap(roomId -> {
                    if (!roomHandler.getRoomStatus(roomId).equals(OPEN)) {
                        return Mono.error(new BadRequest(
                                BUSINESS_LOGIC_EXCEPTION,
                                "Room is not open"));
                    }
                    return Mono.just(roomId);
                })
                .map(roomId -> new Player().setRoomId(roomId))
                .flatMap(playerRepository::save)
                .map(player -> tokenService.generateJwt(
                        player.getRoomId(),
                        player.getId(),
                        List.of(PLAYER)));
    }

    @Transactional
    public Mono<PlayerDto> joinRoom(Long roomId, Long playerId, String username) {
        return getPlayer(playerId)
                .flatMap(player -> checkUsername(player, username))
                .map(player -> player.setUsername(username))
                .flatMap(playerRepository::save)
                .flatMap(any -> answerService.createPlayerScore(roomId, playerId, username))
                .flatMap(any -> roomHandler.addPlayer(roomId, playerId, username))
                .map(playerDto -> playerHandler.addPlayer(roomId, playerDto));
    }

    @Transactional
    public Mono<GeneralResponse> saveAnswer(PublicAppUserDetails userDetails, GiveAnswerRequest answerRequest) {
        return Mono.just(roomHandler.getRoomStatus(userDetails.getRoomId()))
                .flatMap(status -> {
                    if (status != ANSWERING) {
                        return Mono.error(new BadRequest(BUSINESS_LOGIC_EXCEPTION,
                                "Attempt to answer at a wrong stage"));
                    }
                    return Mono.just(status);
                })
                .flatMap(any -> answerService.registerPlayerAnswer(userDetails, answerRequest));
    }

    public Flux<ScoreDto> getScore(Long roomId, Long playerId) {
        return answerService.getScore(roomId, playerId);
    }

    public Flux<RoomStatusResponse> getFluxRoomStatus(Long roomId) {
        return roomHandler.getProcessor(roomId)
                .map(roomStatus -> new RoomStatusResponse()
                        .setRoomStatus(roomStatus));
    }

    public RoomStatus getRoomStatus(Long roomId) {
        return roomHandler.getRoomStatus(roomId);
    }

    public boolean checkKicked(Long roomId, Long userId) {
        return blackListHandler.containsPlayer(roomId, userId);
    }

    private Mono<Player> getPlayer(Long id) {
        return playerRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new BadRequest(
                        BUSINESS_LOGIC_EXCEPTION,
                        format("No user found with id=%d", id))));
    }

    private Mono<Player> checkUsername(Player player, String username) {
        if (isNotBlank(player.getUsername())) {
            return Mono.error(new BadRequest(
                    BUSINESS_LOGIC_EXCEPTION,
                    format("Player with id=%s already has a username", player.getId())));
        }
        if (playerHandler.isUsernameNotAvailable(player.getRoomId(), username)) {
            return Mono.error(new BadRequest(
                    BUSINESS_LOGIC_EXCEPTION,
                    "Username isn't available"));
        }
        return Mono.just(player);
    }

    public Mono<QuizDto> getSimplifiedQuiz(Long roomId) {
        return Mono.just(roomHandler.getRoomStatus(roomId))
                .flatMap(roomStatus -> {
                    if (roomStatus == OPEN) {
                        return Mono.just(roomHandler.getSimplifiedQuiz(roomId));
                    }
                    return Mono.error(new BadRequest(
                            BUSINESS_LOGIC_EXCEPTION,
                            "You can get quiz only when room is OPEN"));
                });
    }

    public void removePlayer(Long roomId, Long userId, String username) {
        Mono.just(roomId)
                .map(id -> {
                    authorService.removePlayer(roomId, userId, username);
                    return roomId;
                })
                .map(id -> {
                    playerHandler.getEvents(roomId).next(
                            new WatchPlayerResponse()
                                    .setPlayerId(userId)
                                    .setUsername(username)
                                    .setStatus(WatchPlayerResponse.Status.REMOVED));
                    return id;
                }).subscribe();
    }

}
