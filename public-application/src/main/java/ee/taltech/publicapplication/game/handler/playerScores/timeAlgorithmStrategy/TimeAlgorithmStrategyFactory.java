package ee.taltech.publicapplication.game.handler.playerScores.timeAlgorithmStrategy;

import ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest;
import ee.taltech.publicapplication.game.model.TimeAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class TimeAlgorithmStrategyFactory {

    private final TimeConstantStrategy timeConstantStrategy;
    private final TimeFastestStrategy timeFastestStrategy;

    public TimeAlgorithmStrategy getStrategy(TimeAlgorithm timeAlgorithm) {
        switch (timeAlgorithm) {
            case CONSTANT:
                return timeConstantStrategy;
            case FASTEST_ANSWER:
                return timeFastestStrategy;
            default:
                throw new BadRequest(BadRequest.Code.BUSINESS_LOGIC_EXCEPTION,
                        format("Unknown time algorithm strategy = %s", timeAlgorithm.toString()));
        }
    }

}
