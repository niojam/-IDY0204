package ee.taltech.publicapplication.game.handler.playerScores.timeAlgorithmStrategy;

import org.springframework.stereotype.Component;

@Component
public class TimeConstantStrategy implements TimeAlgorithmStrategy {

    @Override
    public long calculateReward(long reward, Long timeDiff) {
        return reward;
    }

}
