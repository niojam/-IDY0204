package ee.taltech.publicapplication.game.repository;

import ee.taltech.publicapplication.game.abstract_test.AbstractTestWithRepository;
import ee.taltech.publicapplication.game.model.RoomStatus;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

class RoomRepositoryTest extends AbstractTestWithRepository {

    @Autowired
    RoomRepository roomRepository;

    @Test
    @Description("testcontainers + liquibase + r2dbc integration test")
    void testDbTechsIntegration() {
        roomRepository
                .findRoomByPin("a")
                .as(StepVerifier::create)
                .assertNext(next -> {
                    assertThat(next.getId()).isEqualTo(-1L);
                    assertThat(next.getName()).isEqualTo("super-room");
                    assertThat(next.getPin()).isEqualTo("a");
                    assertThat(next.getQuizId()).isEqualTo(-1L);
                    assertThat(next.getAuthorId()).isEqualTo(-1L);
                    assertThat(next.getStatus()).isEqualTo(RoomStatus.REGISTERED);
                    assertThat(next.getCurrentQuestionId()).isEqualTo(-1L);
                })
                .verifyComplete();
    }

}