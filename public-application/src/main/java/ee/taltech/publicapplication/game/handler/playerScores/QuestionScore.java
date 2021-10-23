package ee.taltech.publicapplication.game.handler.playerScores;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class QuestionScore {

    private int wronglyAnswered;
    private int correctlyAnswered;
    private long reward;

}
