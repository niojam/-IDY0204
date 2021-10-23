package ee.taltech.publicapplication.game.model.dto;

import ee.taltech.publicapplication.game.model.dto.common.GeneralResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class PlayerDto extends GeneralResponse {

    private Long playerId;
    private String username;

}
