package ee.taltech.publicapplication.game.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import ee.taltech.publicapplication.game.model.QuestionType;
import ee.taltech.publicapplication.game.model.TimeAlgorithm;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionDto {

    private Long id;
    private Long quizId;
    private String title;
    private String text;
    private Long imageId;
    private Long timer;
    private Long reward;
    private Long nextQuestionId;
    private List<AnswerDto> answers;
    private QuestionType questionType;
    private TimeAlgorithm timeAlgorithm;

}
