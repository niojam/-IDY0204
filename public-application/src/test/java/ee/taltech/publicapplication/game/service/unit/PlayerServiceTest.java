package ee.taltech.publicapplication.game.service.unit;

import ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest;
import ee.taltech.publicapplication.game.handler.black_list_handler.BlackListHandler;
import ee.taltech.publicapplication.game.handler.player_handler.PlayerHandler;
import ee.taltech.publicapplication.game.handler.room.RoomHandler;
import ee.taltech.publicapplication.game.model.Player;
import ee.taltech.publicapplication.game.model.dto.PlayerDto;
import ee.taltech.publicapplication.game.repository.PlayerRepository;
import ee.taltech.publicapplication.game.security.service.TokenService;
import ee.taltech.publicapplication.game.service.AnswerService;
import ee.taltech.publicapplication.game.service.AuthorService;
import ee.taltech.publicapplication.game.service.PlayerService;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    PlayerService playerService;

    @Mock
    PlayerRepository playerRepository;

    @Mock
    TokenService tokenService;

    @Mock
    RoomHandler roomHandler;

    @Mock
    PlayerHandler playerHandler;

    @Mock
    BlackListHandler blackListHandler;

    @Mock
    AuthorService authorService;

    @Mock
    AnswerService answerService;

    @Captor
    ArgumentCaptor<Player> playerArgumentCaptor;

    @BeforeEach
    void setUpPlayerService() {
        playerService = new PlayerService(
                playerRepository,
                roomHandler,
                blackListHandler,
                playerHandler,
                tokenService,
                answerService,
                authorService
        );
    }

    @Test
    @Description("Should add username")
    void shouldAddUsername() {
        Player withoutUsername = new Player().setId(-1L).setRoomId(-1L);
        Player withUsername = new Player().setId(-1L).setRoomId(-1L).setUsername("player1");
        PlayerDto playerDtoWithUsername = new PlayerDto().setPlayerId(-1L).setUsername("player1");

        when(playerRepository.findById(-1L))
                .thenReturn(Mono.just(withoutUsername));

        when(playerHandler.isUsernameNotAvailable(-1L, "player1"))
                .thenReturn(false);

        when(playerRepository.save(withUsername))
                .thenReturn(Mono.just(withUsername));

        when(answerService.createPlayerScore(-1L, -1L, "player1"))
                .thenReturn(Mono.just(-1L));

        when(roomHandler.addPlayer(-1L, -1L, "player1"))
                .thenReturn(Mono.just(playerDtoWithUsername));

        when(playerHandler.addPlayer(-1L, playerDtoWithUsername)).thenReturn(playerDtoWithUsername);

        playerService.joinRoom(-1L, -1L, "player1")
                .as(StepVerifier::create)
                .expectNext(playerDtoWithUsername)
                .verifyComplete();

        verify(playerRepository).save(playerArgumentCaptor.capture());
        Player capturedPlayer = playerArgumentCaptor.getValue();
        assertThat(capturedPlayer.getUsername()).isEqualTo("player1");
    }

    @Test
    @Description("Should fail username set because user already has a username")
    void shouldFailBecauseUsernameAlreadySet() {
        Player withUsername = new Player().setId(-2L).setRoomId(-2L).setUsername("Super cool username");
        when(playerRepository.findById(-2L)).thenReturn(Mono.just(withUsername));

        playerService.joinRoom(-2L, -2L, "player1")
                .as(StepVerifier::create)
                .expectErrorSatisfies((player) -> assertThat(player)
                        .isInstanceOf(BadRequest.class)
                        .hasMessage("Player with id=-2 already has a username"))
                .verify();
        // TODO fix
    }

    @Test
    @Description("Should fail because user not found")
    void shouldFailNoUserFound() {
        when(playerRepository.findById(-2L))
                .thenReturn(Mono.empty());

        playerService.joinRoom(-2L, -2L, "player1")
                .as(StepVerifier::create)
                .expectErrorSatisfies((player) -> assertThat(player)
                        .isInstanceOf(BadRequest.class)
                        .hasMessage("No user found with id=-2"))
                .verify();
    }

    @Test
    @Description("Should fail because username is not available")
    void shouldNotSetUsernameBecauseItIsNotAvailable() {
        Player withNotAvailableUsername = new Player().setId(-2L).setRoomId(-2L);
        when(playerRepository.findById(-2L)).thenReturn(Mono.just(withNotAvailableUsername));
        when(playerHandler.isUsernameNotAvailable(-2L, "player1")).thenReturn(true);

        playerService.joinRoom(-2L, -2L, "player1")
                .as(StepVerifier::create)
                .expectErrorSatisfies((player) -> assertThat(player)
                        .isInstanceOf(BadRequest.class)
                        .hasMessage("Username isn't available"))
                .verify();
    }

}