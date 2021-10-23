package ee.taltech.publicapplication.game.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Accessors(chain = true)
@Table("kahoot.score")
public class Score {

    @Id
    private Long id;
    private Long playerId;
    private Long roomId;

    private Long score = 0L;
    private Long correctAnswers = 0L;
    private Long wrongAnswers = 0L;

}
