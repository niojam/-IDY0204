package ee.taltech.backoffice.game.model.dto;

import ee.taltech.backoffice.game.model.Question;
import ee.taltech.backoffice.game.model.QuestionType;
import ee.taltech.backoffice.game.model.TimeAlgorithm;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class QuestionDto {

    private Long id;
    private Long quizId;
    private Long imageId;
    private Long nextQuestionId;

    @NotEmpty
    private String text;

    @NotEmpty
    private String title;

    @NotNull
    private Long timer;

    @NotNull
    private Long reward;

    @NotNull
    private QuestionType questionType;

    @NotEmpty
    private List<AnswerDto> answers;

//    @NotNull
    private TimeAlgorithm timeAlgorithm;

    public QuestionDto(Question question) {
        this.id = question.getId();
        this.quizId = question.getQuizId();
        this.imageId = question.getImageId();
        this.nextQuestionId = question.getNextQuestionId();

        this.text = question.getText();
        this.title = question.getTitle();

        this.timer = question.getTimer();
        this.reward = question.getReward();
        this.questionType = question.getQuestionType();
        this.timeAlgorithm = question.getTimeAlgorithm();

        // answers will be set manually while composing a quizDto
    }

}
