package ee.taltech.publicapplication.game.service;

import ee.taltech.publicapplication.game.handler.answer_handler.AnswerHandler;
import ee.taltech.publicapplication.game.handler.black_list_handler.BlackListHandler;
import ee.taltech.publicapplication.game.handler.playerScores.ScoreHandler;
import ee.taltech.publicapplication.game.handler.player_handler.PlayerHandler;
import ee.taltech.publicapplication.game.handler.room.RoomHandler;
import ee.taltech.publicapplication.game.model.Room;
import ee.taltech.publicapplication.game.model.dto.QuizDto;
import ee.taltech.publicapplication.game.model.dto.register_room.RegisterRoomRequest;
import ee.taltech.publicapplication.game.model.dto.top_five_scores.TopScoreDto;
import ee.taltech.publicapplication.game.model.dto.watch_players.WatchPlayerResponse;
import ee.taltech.publicapplication.game.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ee.taltech.publicapplication.game.model.RoomStatus.*;
import static ee.taltech.publicapplication.game.security.model.Authority.AUTHOR;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final Clock clock;
    private final QuizService quizService;
    private final RoomService roomService;
    private final TokenService tokenService;
    private final BlackListHandler blackListHandler;
    private final RoomHandler roomHandler;
    private final ScoreHandler scoreHandler;
    private final AnswerHandler answerHandler;
    private final PlayerHandler playerHandler;
    private final PlayedQuizService playedQuizService;

    public Flux<WatchPlayerResponse> getAllPlayers(Long roomId) {
        return playerHandler.getProcessor(roomId);
    }

    @Transactional
    public Mono<String> registerRoom(Long authorId,
                                     RegisterRoomRequest request) {
        return roomService
                .save(new Room()
                        .setStartedAt(LocalDateTime.now(clock).toInstant(ZoneOffset.UTC))
                        .setAuthorId(authorId)
                        .setStatus(REGISTERED)
                        .setQuizId(request.getQuizId())
                        .setName(request.getRoomName()))
                .map(room -> tokenService.generateJwt(
                        room.getId(),
                        authorId,
                        List.of(AUTHOR)));
    }

    @Transactional
    public Mono<Room> setRoomOpen(Long roomId) {
        return roomService
                .findById(roomId)
                .map(room -> roomService.setStatus(room, OPEN))
                .map(room -> room.setPin(roomService.generateRoomPin(roomHandler.getAllRoomPins())))
                .flatMap(roomService::save)
                .map(blackListHandler::setup)
                .map(roomHandler::setup)
                .map(playerHandler::setup);
    }

    @Transactional
    public Mono<Room> setRoomReady(Long roomId) {
        return roomService
                .findById(roomId)
                .map(room -> roomService.setStatus(room, READY))
                .flatMap(roomService::save)
                .map(playerHandler::completeProcessor)
                .map(room -> roomHandler.setRoomStatus(room, READY))
                .map(room -> roomHandler.emit(room, READY));
    }

    @Transactional
    public Mono<Integer> setRoomAnswering(Long roomId) {
        return roomService
                .findById(roomId)
                .map(room -> roomService.setStatus(room, ANSWERING))
                .map(room -> roomHandler.setQuestionStartedAt(room, clock.millis()))
                .flatMap(roomService::save)
                .map(room -> roomHandler.setRoomStatus(room, ANSWERING))
                .map(room -> roomHandler.emit(room, ANSWERING))
                .map(room -> roomHandler.getPlayersCount(roomId));
    }

    @Transactional
    public Mono<Map<Long, Long>> setRoomReviewing(Long roomId) {
        Long questionIdToShowStatisticsFor = roomHandler.getCurrentQuestionId(roomId);
        return roomService
                .findById(roomId)
                .map(room -> roomService.setStatus(room, REVIEWING))
                .map(room -> scoreHandler.emit(room, true))
                .map(scoreHandler::clearRoomPlayersAnswered)
                .map(room -> room.setCurrentQuestionId(roomHandler.setNextQuestionId(room.getId())))
                .flatMap(roomService::save)
                .map(room -> roomHandler.emit(room, REVIEWING))
                .map(room -> answerHandler.getAnswersStatistics(roomId, questionIdToShowStatisticsFor));
    }

    public void setRoomAborted(Long roomId) {
        // callback should not be reactive. For testing purpose a reactive method is called.
        setRoomAbortedReactive(roomId).subscribe();
    }

    @Transactional
    public Mono<Room> setRoomAbortedReactive(Long roomId) {
        return roomService.findById(roomId)
                .filter(room -> room.getStatus() != FINISHED)
                .map(room -> room.setStatus(ABORTED))
                .flatMap(roomService::save)
                .map(room -> roomHandler.emit(room, ABORTED))
                .map(this::clean);
    }

    @Transactional
    public Mono<Room> setRoomFinished(Long roomId) {
        return roomService.findById(roomId)
                .map(room -> roomService.setStatus(room, FINISHED))
                .flatMap(roomService::save)
                .flatMap(scoreHandler::saveRoomScore)
                .flatMap(playedQuizService::saveQuiz)
                .flatMap(roomService::save)
                .map(room -> roomHandler.setRoomStatus(room, FINISHED))
                .map(room -> roomHandler.emit(room, FINISHED))
                .map(this::clean);
    }

    public Mono<Long> removePlayer(Long roomId, Long playerId, String username) {
        roomHandler.removePlayer(roomId, playerId);
        playerHandler.removePlayer(roomId, playerId, username);
        scoreHandler.removePlayerStatistics(roomId, playerId);
        blackListHandler.addPlayer(roomId, playerId);
        return Mono.just(roomId);
    }

    public Mono<QuizDto> getQuiz(Long roomId) {
        return roomService.findById(roomId)
                .map(Room::getQuizId)
                .flatMap(quizService::findById)
                .map(quiz -> {
                    roomHandler.addQuiz(roomId, quiz);
                    scoreHandler.setup(roomId);
                    answerHandler.setup(roomId, quiz);
                    return quiz;
                });
    }

    public Mono<List<TopScoreDto>> getTopNScores(Long roomId, Integer n) {
        return Mono.just(scoreHandler.getTopNScores(roomId, n))
                .map(topScores -> topScores.stream()
                        .map(inMemoryScore -> new TopScoreDto()
                                .setUsername(inMemoryScore.getUsername())
                                .setReward(inMemoryScore.getReward())
                                .setTotalScore(inMemoryScore.getTotalScore()))
                        .collect(Collectors.toList()));
    }

    public Flux<Long> countAnswers(Long roomId) {
        return answerHandler.getProcessor(roomId);
    }

    private Room clean(Room room) {
        playerHandler.clean(room);
        roomHandler.clean(room);
        blackListHandler.clean(room);
        answerHandler.clean(room);
        scoreHandler.clean(room);
        return room;
    }

}
