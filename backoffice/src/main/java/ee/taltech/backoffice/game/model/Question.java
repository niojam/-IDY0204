package ee.taltech.backoffice.game.model;

import ee.taltech.backoffice.game.model.dto.QuestionDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class Question {

    @Id
    private Long id;
    private String title;
    private String text;
    private Long quizId;
    private Long timer = 45L;
    private Long reward = 100L;
    private Long nextQuestionId;
    private Long imageId;
    private TimeAlgorithm timeAlgorithm;
    private QuestionType questionType;

    public Question(QuestionDto dto) {
        this.id = dto.getId();
        this.title = dto.getTitle();
        this.text = dto.getText();
        this.quizId = dto.getQuizId();
        this.imageId = dto.getQuizId();
        this.questionType = dto.getQuestionType();
        this.timer = dto.getTimer();
        this.reward = dto.getReward();
        this.timeAlgorithm = dto.getTimeAlgorithm();
    }

}
