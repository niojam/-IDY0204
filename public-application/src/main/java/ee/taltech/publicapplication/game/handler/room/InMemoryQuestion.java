package ee.taltech.publicapplication.game.handler.room;

import ee.taltech.publicapplication.game.model.QuestionType;
import ee.taltech.publicapplication.game.model.TimeAlgorithm;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class InMemoryQuestion {

    private Long nextQuestionId;
    private Set<Long> correctAnswers;
    private Long reward;
    private QuestionType questionType;
    private TimeAlgorithm timeAlgorithm;
    private Long startedAt;

}
