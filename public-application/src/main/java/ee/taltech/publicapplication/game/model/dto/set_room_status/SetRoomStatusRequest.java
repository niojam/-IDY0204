package ee.taltech.publicapplication.game.model.dto.set_room_status;

import ee.taltech.publicapplication.game.model.RoomStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class SetRoomStatusRequest {

    @NotNull
    private RoomStatus roomStatus;

}
