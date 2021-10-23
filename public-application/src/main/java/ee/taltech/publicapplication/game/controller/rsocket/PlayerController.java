package ee.taltech.publicapplication.game.controller.rsocket;

import ee.taltech.publicapplication.game.controller.errorHandling.rsocket.RSocketExceptionHandler;
import ee.taltech.publicapplication.game.model.dto.PlayerDto;
import ee.taltech.publicapplication.game.model.dto.QuizDto;
import ee.taltech.publicapplication.game.model.dto.ScoreDto;
import ee.taltech.publicapplication.game.model.dto.common.GeneralResponse;
import ee.taltech.publicapplication.game.model.dto.give_answer.GiveAnswerRequest;
import ee.taltech.publicapplication.game.model.dto.join_room.JoinRoomRequest;
import ee.taltech.publicapplication.game.model.dto.room_status.RoomStatusResponse;
import ee.taltech.publicapplication.game.security.user_details.PublicAppUserDetails;
import ee.taltech.publicapplication.game.service.PlayerService;
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

import static ee.taltech.publicapplication.game.model.RoomStatus.OPEN;
import static ee.taltech.publicapplication.game.model.dto.common.ResponseStatus.COMPLETED;
import static ee.taltech.publicapplication.game.model.dto.common.ResponseStatus.KICKED;

@Validated
@Controller
@RequiredArgsConstructor
public class PlayerController extends RSocketExceptionHandler {

    private final PlayerService playerService;

    @MessageMapping("join-room")
    @PreAuthorize("hasRole('PLAYER')")
    public Mono<PlayerDto> joinRoom(@AuthenticationPrincipal PublicAppUserDetails userDetails,
                                    @Valid JoinRoomRequest request) {
        return playerService.joinRoom(
                userDetails.getRoomId(),
                userDetails.getUserId(),
                request.getUsername());
    }

    @MessageMapping("get-room-status")
    @PreAuthorize("hasRole('PLAYER')")
    public Flux<RoomStatusResponse> getRoomStatus(@AuthenticationPrincipal PublicAppUserDetails userDetails) {
        return playerService.getFluxRoomStatus(userDetails.getRoomId())
                .doOnCancel(() -> playerService.removePlayer(
                        userDetails.getRoomId(),
                        userDetails.getUserId(),
                        userDetails.getUsername()
                ));
    }

    @MessageMapping("give-answer")
    @PreAuthorize("hasRole('PLAYER')")
    public Mono<GeneralResponse> giveAnswer(@AuthenticationPrincipal PublicAppUserDetails userDetails,
                                            @Valid GiveAnswerRequest answerRequest) {
        return playerService.saveAnswer(userDetails, answerRequest);
    }

    @MessageMapping("get-score")
    @PreAuthorize("hasRole('PLAYER')")
    public Flux<ScoreDto> getScore(@AuthenticationPrincipal PublicAppUserDetails userDetails) {
        return playerService.getScore(userDetails.getRoomId(), userDetails.getUserId());
    }

    @MessageMapping("get-simplified-quiz")
    @PreAuthorize("hasRole('PLAYER')")
    public Mono<QuizDto> getQuiz(@AuthenticationPrincipal PublicAppUserDetails userDetails) {
        return playerService.getSimplifiedQuiz(userDetails.getRoomId());
    }

    @MessageMapping("player-ping")
    public Flux<GeneralResponse> pingRoom(@AuthenticationPrincipal PublicAppUserDetails userDetails) {
        return Flux.interval(Duration.ofSeconds(2L))
                .map(tick -> {
                    boolean isKicked = playerService.checkKicked(userDetails.getRoomId(), userDetails.getUserId());
                    if (!playerService.getRoomStatus(userDetails.getRoomId()).equals(OPEN)) {
                        return new GeneralResponse().setResponseStatus(COMPLETED);
                    }
                    return isKicked ? new GeneralResponse().setResponseStatus(KICKED) : new GeneralResponse();
                })
                .takeUntil(response -> response.getResponseStatus().equals(COMPLETED))
                .doOnCancel(() -> playerService.removePlayer(
                        userDetails.getRoomId(),
                        userDetails.getUserId(),
                        userDetails.getUsername()
                ));
    }

}
