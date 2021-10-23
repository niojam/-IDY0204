package ee.taltech.publicapplication.game.handler.playerScores.quetstionTypeStrategy;

import ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest;
import ee.taltech.publicapplication.game.handler.playerScores.QuestionScore;

import java.util.Set;

public abstract class QuestionTypeStrategy {

    public QuestionScore calculateReward(long reward, Set<Long> correctAnswersIds, Set<Long> givenAnswersIds) {
        int correctAnswersAmount = correctAnswersIds.size();

        if (correctAnswersAmount < 1) {
            throw new BadRequest(BadRequest.Code.BUSINESS_LOGIC_EXCEPTION, "Question without correct answers");
        }

        long singleAnswerReward = reward / correctAnswersAmount;

        return calculateSum(singleAnswerReward, correctAnswersIds, givenAnswersIds);
    }

    abstract QuestionScore calculateSum(long singleAnswerReward, Set<Long> correctAnswersIds, Set<Long> givenAnswersIds);

}
