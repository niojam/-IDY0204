package ee.taltech.publicapplication.game.controller.rsocket;

import ee.taltech.publicapplication.game.controller.errorHandling.rsocket.RSocketExceptionHandler;
import ee.taltech.publicapplication.game.model.dto.QuizDto;
import ee.taltech.publicapplication.game.model.dto.common.GeneralResponse;
import ee.taltech.publicapplication.game.model.dto.remove_player.KickPlayerRequest;
import ee.taltech.publicapplication.game.model.dto.room_status.RoomStatusResponse;
import ee.taltech.publicapplication.game.model.dto.room_status.RoomStatusResponseWithPlayerCount;
import ee.taltech.publicapplication.game.model.dto.room_status.RoomStatusResponseWithRoomPin;
import ee.taltech.publicapplication.game.model.dto.room_status.RoomStatusResponseWithStatistics;
import ee.taltech.publicapplication.game.model.dto.top_five_scores.TopScoresResponse;
import ee.taltech.publicapplication.game.model.dto.watch_players.WatchPlayerResponse;
import ee.taltech.publicapplication.game.security.user_details.PublicAppUserDetails;
import ee.taltech.publicapplication.game.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.Duration;

import static ee.taltech.publicapplication.game.model.RoomStatus.*;

@Validated
@Controller
@RequiredArgsConstructor
public class AuthorController extends RSocketExceptionHandler {

    private final AuthorService authorService;

    @MessageMapping("watch-players")
    @PreAuthorize("hasRole('AUTHOR')")
    public Flux<WatchPlayerResponse> getAllPlayers(@AuthenticationPrincipal PublicAppUserDetails userDetails) {
        return authorService.getAllPlayers(userDetails.getRoomId());
    }

    @MessageMapping("set-open")
    @PreAuthorize("hasRole('AUTHOR')")
    public Mono<RoomStatusResponseWithRoomPin> setRoomOpen(@AuthenticationPrincipal PublicAppUserDetails userDetails) {
        return authorService
                .setRoomOpen(userDetails.getRoomId())
                .map(room -> new RoomStatusResponseWithRoomPin(OPEN)
                        .setRoomPin(room.getPin()));
    }

    @MessageMapping("set-ready")
    @PreAuthorize("hasRole('AUTHOR')")
    public Mono<RoomStatusResponse> setRoomReady(@AuthenticationPrincipal PublicAppUserDetails userDetails) {
        return authorService.setRoomReady(userDetails.getRoomId())
                .map(room -> new RoomStatusResponse(READY));
    }

    @MessageMapping("set-answering")
    @PreAuthorize("hasRole('AUTHOR')")
    public Mono<RoomStatusResponseWithPlayerCount> setRoomAnswering(@AuthenticationPrincipal PublicAppUserDetails userDetails) {
        return authorService.setRoomAnswering(userDetails.getRoomId())
                .map(playersCount -> new RoomStatusResponseWithPlayerCount(ANSWERING)
                        .setPlayersCount(playersCount)
                );
    }

    @MessageMapping("set-reviewing")
    @PreAuthorize("hasRole('AUTHOR')")
    public Mono<RoomStatusResponseWithStatistics> setRoomReviewing(@AuthenticationPrincipal PublicAppUserDetails userDetails) {
        return authorService.setRoomReviewing(userDetails.getRoomId())
                .map(statistics -> new RoomStatusResponseWithStatistics()
                        .setRoomStatus(REVIEWING)
                        .setStatistics(statistics));
    }

    @MessageMapping("set-finished")
    @PreAuthorize("hasRole('AUTHOR')")
    public Mono<RoomStatusResponse> setRoomFinished(@AuthenticationPrincipal PublicAppUserDetails userDetails) {
        return authorService.setRoomFinished(userDetails.getRoomId())
                .map(any -> new RoomStatusResponse().setRoomStatus(FINISHED));
    }

    @MessageMapping("kick-player")
    @PreAuthorize("hasRole('AUTHOR')")
    public Mono<GeneralResponse> kickPlayer(@AuthenticationPrincipal PublicAppUserDetails userDetails,
                                            @Valid KickPlayerRequest request) {
        return authorService
                .removePlayer(userDetails.getRoomId(), request.getPlayerId(), request.getPlayerUsername())
                .map(any -> new GeneralResponse());
    }

    @MessageMapping("author-ping")
    @PreAuthorize("hasRole('AUTHOR')")
    public Flux<GeneralResponse> getRoomStatus(@AuthenticationPrincipal PublicAppUserDetails userDetails) {
        return Flux.interval(Duration.ofSeconds(2L))
                .map(tick -> new GeneralResponse())
                .doOnCancel(() -> authorService.setRoomAborted(userDetails.getRoomId()));
    }

    @MessageMapping("get-quiz")
    @PreAuthorize("hasRole('AUTHOR')")
    public Mono<QuizDto> getQuiz(@AuthenticationPrincipal PublicAppUserDetails userDetails) {
        return authorService.getQuiz(userDetails.getRoomId());
    }

    @MessageMapping("get-top-5")
    @PreAuthorize("hasRole('AUTHOR')")
    public Mono<TopScoresResponse> getTopFiveScores(@AuthenticationPrincipal PublicAppUserDetails userDetails) {
        return authorService.getTopNScores(userDetails.getRoomId(), 5)
                .map(topScoreList -> new TopScoresResponse().setTopScores(topScoreList));
    }

    @MessageMapping("get-top-3")
    @PreAuthorize("hasRole('AUTHOR')")
    public Mono<TopScoresResponse> getTopThreeScores(@AuthenticationPrincipal PublicAppUserDetails userDetails) {
        return authorService.getTopNScores(userDetails.getRoomId(), 3)
                .map(topScoreList -> new TopScoresResponse().setTopScores(topScoreList));
    }

    @MessageMapping("count-answers")
    @PreAuthorize("hasRole('AUTHOR')")
    public Flux<GeneralResponse> countAnswers(@AuthenticationPrincipal PublicAppUserDetails userDetails) {
        return authorService.countAnswers(userDetails.getRoomId())
                .map(any -> new GeneralResponse());
    }

}
