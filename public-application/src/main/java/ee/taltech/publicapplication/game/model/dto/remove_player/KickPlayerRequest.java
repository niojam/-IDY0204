package ee.taltech.publicapplication.game.model.dto.remove_player;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class KickPlayerRequest {

    @NotNull
    private Long playerId;

    @NotBlank
    private String playerUsername;

}
