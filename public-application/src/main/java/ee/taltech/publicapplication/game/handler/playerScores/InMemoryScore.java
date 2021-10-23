package ee.taltech.publicapplication.game.handler.playerScores;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class InMemoryScore {

    private Long playerId;
    private String username;
    private Long reward = 0L;
    private Long totalScore = 0L;
    private Long correctAnswers = 0L;
    private Long wrongAnswers = 0L;

    public void increaseScore(Long increaseBy) {
        totalScore += increaseBy;
    }

    public void correctlyAnswered(int amount) {
        correctAnswers += amount;
    }

    public void wronglyAnswered(int amount) {
        wrongAnswers += amount;
    }

}
