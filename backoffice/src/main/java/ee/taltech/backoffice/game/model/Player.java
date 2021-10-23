package ee.taltech.backoffice.game.model;

import ee.taltech.backoffice.game.model.dto.PlayerDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Player {

    @Id
    private Long id;

    private String username;

    private Long roomId;


    public Player(PlayerDto dto) {
        this.id = dto.getId();
        this.roomId = dto.getId();
        this.username = dto.getUsername();
    }

}
