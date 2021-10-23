package ee.taltech.publicapplication.game.handler.room;

import ee.taltech.publicapplication.game.model.dto.QuizDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class InMemoryQuiz {

    private Map<Long, InMemoryQuestion> inMemoryQuestionMap = new HashMap<>(); // <questionId, InMemoryQuestion>
    private QuizDto simplifiedQuizDto;

}
