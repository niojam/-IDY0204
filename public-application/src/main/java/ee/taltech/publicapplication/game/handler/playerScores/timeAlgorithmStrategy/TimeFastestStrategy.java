package ee.taltech.publicapplication.game.handler.playerScores.timeAlgorithmStrategy;

import org.springframework.stereotype.Component;

@Component
public class TimeFastestStrategy implements TimeAlgorithmStrategy {

    @Override
    public long calculateReward(long reward, Long timeDiff) {
        long penalty = (reward / 100) * (timeDiff / 1000);
        long result = reward - penalty;
        return result < 0 ? 0 : result;
    }

}
