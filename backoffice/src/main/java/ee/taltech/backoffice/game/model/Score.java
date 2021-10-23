package ee.taltech.backoffice.game.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;


@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Score {

    @Id
    private Long id;
    private Long correctAnswers;
    private Long wrongAnswers;
    private Long score;
    private Long playerId;
    private Long roomId;

}
