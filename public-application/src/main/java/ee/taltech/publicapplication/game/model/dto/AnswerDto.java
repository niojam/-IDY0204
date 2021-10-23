package ee.taltech.publicapplication.game.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerDto {

    private Long id;
    private String text;
    private Long questionId;
    private Boolean isCorrect;

}
