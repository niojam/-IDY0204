package ee.taltech.backoffice.game.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class QuizDetails {

    private Long quizId;
    private Long imageId;
    private String quizName;

}
