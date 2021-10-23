package ee.taltech.publicapplication.game.model.dto.register_room;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class RegisterRoomRequest {

    @NotNull
    private Long quizId;

    @NotBlank
    private String roomName;

}
