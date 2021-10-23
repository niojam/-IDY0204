package ee.taltech.publicapplication.game.service.integration;

import ee.taltech.publicapplication.game.abstract_test.AbstractTestWithRepository;
import ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest;
import ee.taltech.publicapplication.game.handler.room.RoomHandler;
import ee.taltech.publicapplication.game.model.Player;
import ee.taltech.publicapplication.game.repository.PlayerRepository;
import ee.taltech.publicapplication.game.security.properties.SecurityProperties;
import ee.taltech.publicapplication.game.service.PlayerService;
import io.jsonwebtoken.Jwts;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest.Code.BUSINESS_LOGIC_EXCEPTION;
import static ee.taltech.publicapplication.game.security.utils.JwtUtils.publicKey;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PlayerServiceIntegrationTest extends AbstractTestWithRepository {

    @Autowired
    PlayerService playerService;

    @Autowired
    SecurityProperties securityProperties;

    @Mock
    RoomHandler roomHandler;

    @MockBean
    PlayerRepository playerRepository;

    @Test
    @Description("Should generate access token if provided password is correct")
    void shouldGenerateAccessTokenWithValidPassword() {
        when(roomHandler.getRoomId("0123456789")).thenReturn(-2L);
        when(playerRepository.save(any()))
                .thenReturn(Mono.just(new Player()
                        .setId(-15L)
                        .setRoomId(-2L)
                        .setUsername("test-player")));

        playerService.accessRoom("0123456789")
                .map(token -> Jwts.parser()
                        .setSigningKey(publicKey(securityProperties.getPublicKey()))
                        .parseClaimsJws(token))
                .as(StepVerifier::create)
                .assertNext((parsedToken) -> assertThat(parsedToken.getBody().get("playerId"))
                        .isEqualTo("-15"));
    }

    @Test
    @Description("Should throw exception in case if password is wrong")
    void shouldFailIfPasswordIsIncorrect() {
        when(roomHandler.getRoomId("wrong password")).thenThrow(new BadRequest(
                BUSINESS_LOGIC_EXCEPTION,
                format("No room found with roomPin=%s", "wrong password")));

        playerService.accessRoom("wrong password")
                .as(StepVerifier::create)
                .expectErrorSatisfies((error) -> assertThat(error)
                        .isInstanceOf(BadRequest.class)
                        .hasMessage("No room found with roomPin=wrong password"))
                .verify();
    }

}