package ee.taltech.publicapplication.game.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("kahoot.player")
@Accessors(chain = true)
public class Player {

    @Id
    private Long id;
    private Long roomId;
    private String username;

}
