package ee.taltech.publicapplication.game.handler.playerScores.quetstionTypeStrategy;

import ee.taltech.publicapplication.game.handler.playerScores.QuestionScore;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MultipleMatchStrategy extends QuestionTypeStrategy {

    public QuestionScore calculateSum(long singleAnswerReward, Set<Long> correctAnswersIds, Set<Long> givenAnswersIds) {
        int sum = 0;
        int wronglyAnswered = 0;
        int correctlyAnswered = 0;

        for (Long answerId : givenAnswersIds) {
            if (correctAnswersIds.contains(answerId)) {
                sum += singleAnswerReward;
                correctlyAnswered++;
            } else {
                wronglyAnswered++;
            }
        }

        return new QuestionScore()
                .setReward(wronglyAnswered > 0 ? 0 : sum)
                .setCorrectlyAnswered(correctlyAnswered)
                .setWronglyAnswered(wronglyAnswered);
    }

}
