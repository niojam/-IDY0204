package ee.taltech.publicapplication.game.model.dto.start_room;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class RoomIdRequest {

    @NotNull
    private Long roomId;

}
