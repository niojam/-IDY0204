package ee.taltech.backoffice.game.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PlayerStatistics {

    private String username;
    private Long correctAnswers;
    private Long wrongAnswers;
    private Long score;
    private Long roomId;
}
