package ee.taltech.publicapplication.game.handler.playerScores.quetstionTypeStrategy;

import ee.taltech.publicapplication.game.handler.playerScores.QuestionScore;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MultipleAnyStrategy extends QuestionTypeStrategy {

    public QuestionScore calculateSum(long singleAnswerReward, Set<Long> correctAnswersIds, Set<Long> givenAnswersIds) {
        int sum = 0;
        int wronglyAnswered = 0;
        int correctlyAnswered = 0;

        for (Long answerId : givenAnswersIds) {
            if (correctAnswersIds.contains(answerId)) {
                correctlyAnswered++;
                sum += singleAnswerReward;
            } else {
                wronglyAnswered++;
                sum -= singleAnswerReward;
            }
        }
        return new QuestionScore()
                .setReward(sum)
                .setCorrectlyAnswered(correctlyAnswered)
                .setWronglyAnswered(wronglyAnswered);
    }

}
