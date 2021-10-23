package ee.taltech.backoffice.game.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class QuestionStatisticsDto {

    private Long questionId;
    private String questionTitle;
    private String questionText;
    private List<AnswerFrequencyDto> answerFrequencies;

}
