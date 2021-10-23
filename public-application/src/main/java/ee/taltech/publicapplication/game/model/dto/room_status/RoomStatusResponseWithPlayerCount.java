package ee.taltech.publicapplication.game.model.dto.room_status;

import ee.taltech.publicapplication.game.model.RoomStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RoomStatusResponseWithPlayerCount extends RoomStatusResponse {

    private int playersCount;

    public RoomStatusResponseWithPlayerCount(RoomStatus roomStatus) {
        super(roomStatus);
    }

}
