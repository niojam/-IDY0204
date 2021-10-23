package ee.taltech.backoffice.game.model.dto;

import ee.taltech.backoffice.game.model.QuestionType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class QuestionDetails {

    private Long id;
    private String title;
    private QuestionType questionType;

}
