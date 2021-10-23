package ee.taltech.publicapplication.game.handler.playerScores.quetstionTypeStrategy;

import ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest;
import ee.taltech.publicapplication.game.model.QuestionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class QuestionTypeStrategyFactory {

    private final SingleMatchStrategy singleMatchStrategy;
    private final MultipleAnyStrategy multipleAnyStrategy;
    private final MultipleMatchStrategy multipleMatchStrategy;

    public QuestionTypeStrategy getStrategy(QuestionType questionType) {
        switch (questionType) {
            case SINGLE_MATCH:
                return singleMatchStrategy;
            case MULTIPLE_ANY:
                return multipleAnyStrategy;
            case MULTIPLE_MATCH:
                return multipleMatchStrategy;
            default:
                throw new BadRequest(BadRequest.Code.BUSINESS_LOGIC_EXCEPTION,
                        format("Unknown questionType = %s", questionType.toString()));
        }
    }

}
