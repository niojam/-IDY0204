package ee.taltech.publicapplication.game.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import ee.taltech.publicapplication.game.model.dto.common.GeneralResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuizDto extends GeneralResponse {

    private String name;
    private Long authorId;
    private Long firstQuestionId;
    private List<QuestionDto> questions;

}
