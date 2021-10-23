package ee.taltech.publicapplication.game.handler.playerScores.timeAlgorithmStrategy;

public interface TimeAlgorithmStrategy {

    long calculateReward(long reward, Long timeDiff);

}
