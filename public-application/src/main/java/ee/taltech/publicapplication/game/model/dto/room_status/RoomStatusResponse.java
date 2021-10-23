package ee.taltech.publicapplication.game.model.dto.room_status;

import ee.taltech.publicapplication.game.model.RoomStatus;
import ee.taltech.publicapplication.game.model.dto.common.GeneralResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RoomStatusResponse extends GeneralResponse {

    private RoomStatus roomStatus;

}
