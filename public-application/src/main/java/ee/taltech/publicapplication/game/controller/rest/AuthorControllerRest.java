package ee.taltech.publicapplication.game.controller.rest;

import ee.taltech.publicapplication.config.ApplicationConfig;
import ee.taltech.publicapplication.game.model.dto.register_room.RegisterRoomRequest;
import ee.taltech.publicapplication.game.model.dto.room_status.RoomStatusResponseWithRelocation;
import ee.taltech.publicapplication.game.security.user_details.TokenExchangeUserDetails;
import ee.taltech.publicapplication.game.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.cookie.StandardCookieSpec.STRICT;
import static ee.taltech.publicapplication.common.Constants.ACCESS_TOKEN_COOKIE;

@RestController
@RequiredArgsConstructor
@RequestMapping("author")
public class AuthorControllerRest {

    private final AuthorService authorService;
    private final ApplicationConfig.GameProperties properties;

    @PostMapping("register-room")
    @PreAuthorize("hasRole('AUTHOR')")
    public Mono<RoomStatusResponseWithRelocation> registerRoom(@AuthenticationPrincipal TokenExchangeUserDetails userDetails,
                                                               @RequestBody @Valid RegisterRoomRequest request,
                                                               ServerHttpResponse response) {
        return authorService
                .registerRoom(userDetails.getUserId(), request)
                .map(token -> ResponseCookie
                        .from(ACCESS_TOKEN_COOKIE, token)
                        .sameSite(STRICT)
                        .path("/")
                        .domain(properties.getDomain())
                        .build())
                .flatMap(cookie -> {
                    response.addCookie(cookie);
                    return Mono.just(new RoomStatusResponseWithRelocation(properties.getRoomRegisteredRerouteLocation()));
                });
    }

}
