package ee.taltech.backoffice.game.model.dto;

import ee.taltech.backoffice.game.model.Answer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AnswerDto {

    private Long id;
    private String text;
    private Long questionId;
    private Boolean isCorrect;


    public AnswerDto(Answer answer) {
        this.id = answer.getId();
        this.text = answer.getText();
        this.questionId = answer.getQuestionId();
        this.isCorrect = answer.getIsCorrect();
    }
}
