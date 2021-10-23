package ee.taltech.publicapplication.game.model.dto.access_room;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
public class AccessRoomRequest {

    @NotBlank
    @Size(max = 6, min = 6)
    private String pin;

}
