package ee.taltech.backoffice.game.model;

import ee.taltech.backoffice.game.model.dto.AnswerDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Answer {

    @Id
    private Long id;

    private String text;

    private Long questionId;
    private Boolean isCorrect;

    public Answer(AnswerDto dto) {
        this.id = dto.getId();
        this.text = dto.getText();
        this.questionId = dto.getQuestionId();
        this.isCorrect = dto.getIsCorrect();
    }

}
