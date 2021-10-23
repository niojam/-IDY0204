package ee.taltech.publicapplication.game.model.dto.room_status;

import ee.taltech.publicapplication.game.model.RoomStatus;
import ee.taltech.publicapplication.game.model.dto.common.GeneralResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RoomStatusResponseWithStatistics extends GeneralResponse {

    private RoomStatus roomStatus;
    private Map<Long, Long> statistics;

}
