package ee.taltech.publicapplication.game.controller.rest;

import ee.taltech.publicapplication.game.model.dto.access_room.AccessRoomRequest;
import ee.taltech.publicapplication.game.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("player")
@RequiredArgsConstructor
public class PlayerControllerRest {

    private final PlayerService playerService;

    @PostMapping("access")
    public Mono<Void> accessRoom(@RequestBody @Valid AccessRoomRequest request,
                                 ServerHttpResponse response) {
        return playerService.accessRoom(request.getPin())
                .flatMap(token -> {
                    response.getHeaders().add(AUTHORIZATION, token);
                    return Mono.empty();
                });
    }

}
