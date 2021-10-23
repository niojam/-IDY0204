package ee.taltech.backoffice.game.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AnswerFrequencyDto {

    private Long questionId;
    private String questionTitle;
    private String questionText;
    private String answerText;
    private Long frequency;
    private boolean isCorrect;
}
