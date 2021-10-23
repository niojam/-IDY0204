package ee.taltech.publicapplication.game.controller;

import ee.taltech.publicapplication.config.WebfluxSecurity;
import ee.taltech.publicapplication.game.controller.rest.PlayerControllerRest;
import ee.taltech.publicapplication.game.model.dto.access_room.AccessRoomRequest;
import ee.taltech.publicapplication.game.service.PlayerService;
import jdk.jfr.Description;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import(WebfluxSecurity.class)
@WebFluxTest(controllers = PlayerControllerRest.class)
class PlayerControllerRestTest {

    @Autowired
    WebTestClient webClient;

    @MockBean
    PlayerService playerService;

    @Test
    @Ignore
    @Description("Should process request")
    void shouldSuccessfullyProcessRequest() {
        AccessRoomRequest request = new AccessRoomRequest().setPin("123456");
        when(playerService.accessRoom("123456"))
                .thenReturn(Mono.just("token"));
        webClient.post()
                .uri("/player/access")
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isOk();

    }

}