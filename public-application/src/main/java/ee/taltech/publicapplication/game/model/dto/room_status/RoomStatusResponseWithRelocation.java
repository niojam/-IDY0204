package ee.taltech.publicapplication.game.model.dto.room_status;

import ee.taltech.publicapplication.game.model.RoomStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoomStatusResponseWithRelocation extends RoomStatusResponse {

    private String relocationUrl;

    public RoomStatusResponseWithRelocation(String url) {
        super(RoomStatus.REGISTERED);
        this.relocationUrl = url;
    }

}
