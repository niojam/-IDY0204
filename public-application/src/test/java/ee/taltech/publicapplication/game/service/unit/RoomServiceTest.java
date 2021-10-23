package ee.taltech.publicapplication.game.service.unit;

import ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest;
import ee.taltech.publicapplication.game.model.Room;
import ee.taltech.publicapplication.game.repository.RoomRepository;
import ee.taltech.publicapplication.game.service.RoomService;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static ee.taltech.publicapplication.game.model.RoomStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    RoomService roomService;

    @Mock
    RoomRepository roomRepository;

    @BeforeEach
    void setUpRoomServiceTest() {
        roomService = new RoomService(roomRepository);
    }

    @Test
    @Description("Should not set room status to OPEN")
    void shouldNotSetOpen() {
        assertThatThrownBy(() -> roomService.setStatus(new Room().setStatus(OPEN), OPEN))
                .isInstanceOf(BadRequest.class)
                .hasMessage("Cannot set room status to OPEN");
    }

    @Test
    @Description("Should not set room status to READY")
    void shouldNotSetReady() {
        assertThatThrownBy(() -> roomService.setStatus(new Room().setStatus(ANSWERING), READY))
                .isInstanceOf(BadRequest.class)
                .hasMessage("Cannot set room status to READY");
    }

    @Test
    @Description("Should set room status to READY")
    void shouldSetToReady() {
        Room room = roomService.setStatus(new Room().setStatus(OPEN), READY);
        assertThat(room).isEqualTo(new Room().setStatus(READY));
    }

    @Test
    @Description("Should not set room status to ANSWERING")
    void shouldNotSetAnswering() {
        assertThatThrownBy(() -> roomService.setStatus(new Room().setStatus(OPEN), ANSWERING))
                .isInstanceOf(BadRequest.class)
                .hasMessage("Cannot set room status to ANSWERING");
    }

    @Test
    @Description("Should set room status to ANSWERING with current REVIEWING")
    void shouldSetAnsweringWithCurrentReviewing() {
        Room room = roomService.setStatus(new Room().setCurrentQuestionId(-2L).setStatus(REVIEWING), ANSWERING);
        assertThat(room).isEqualTo(new Room().setStatus(ANSWERING).setCurrentQuestionId(-2L));
    }

    @Test
    @Description("Should not set room status to ANSWERING with current REVIEWING because no current quesiton")
    void shouldNotSetAnsweringWithCurrentReviewing() {
        assertThatThrownBy(() -> roomService.setStatus(new Room().setStatus(REVIEWING), ANSWERING))
                .isInstanceOf(BadRequest.class)
                .hasMessage("You cannot set room to ANSWERING because all question are answered");
    }

    @Test
    @Description("Should set room status to ANSWERING with current REVIEWING")
    void shouldSetAnsweringWithCurrentReady() {
        Room room = roomService.setStatus(new Room().setStatus(READY), ANSWERING);
        assertThat(room).isEqualTo(new Room().setStatus(ANSWERING));
    }

    @Test
    @Description("Should not set room status to REVIEWING")
    void shouldNotSetReviewing() {
        assertThatThrownBy(() -> roomService.setStatus(new Room().setStatus(FINISHED), REVIEWING))
                .isInstanceOf(BadRequest.class)
                .hasMessage("Cannot set room status to REVIEWING");
    }

    @Test
    @Description("Should set room status to REVIEWING")
    void shouldSetReviewing() {
        Room room = roomService.setStatus(new Room().setStatus(ANSWERING), REVIEWING);
        assertThat(room).isEqualTo(new Room().setStatus(REVIEWING));
    }

}
