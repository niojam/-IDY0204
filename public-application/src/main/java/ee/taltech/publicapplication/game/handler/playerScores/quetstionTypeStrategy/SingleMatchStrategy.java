package ee.taltech.publicapplication.game.handler.playerScores.quetstionTypeStrategy;

import ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest;
import ee.taltech.publicapplication.game.handler.playerScores.QuestionScore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;

@Component
public class SingleMatchStrategy extends QuestionTypeStrategy {

    public QuestionScore calculateSum(long singleAnswerReward, Set<Long> correctAnswersIds, Set<Long> givenAnswersIds) {
        if (correctAnswersIds.size() > 1) {
            throw new BadRequest(BadRequest.Code.BUSINESS_LOGIC_EXCEPTION, "More than one answer is given");
        }
        boolean correct = correctAnswersIds.contains(new ArrayList<>(givenAnswersIds).get(0));
        return new QuestionScore()
                .setReward(correct ? singleAnswerReward : 0)
                .setCorrectlyAnswered(correct ? 1 : 0)
                .setWronglyAnswered(correct ? 0 : 1);
    }

}
