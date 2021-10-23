package ee.taltech.backoffice.game.model.dto;


import ee.taltech.backoffice.game.model.Player;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PlayerDto {

    private Long id;
    private String username;
    private Long roomId;

    public PlayerDto (Player player) {
        this.id = player.getId();
        this.username = player.getUsername();
        this.roomId = player.getRoomId();
    }

}
