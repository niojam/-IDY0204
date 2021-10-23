package ee.taltech.publicapplication.game.model.dto.join_room;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
public class JoinRoomRequest {

    @NotBlank
    @Size(max = 15)
    private String username;

}
