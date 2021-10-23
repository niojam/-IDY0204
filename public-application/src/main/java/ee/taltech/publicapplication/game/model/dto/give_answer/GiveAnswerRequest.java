package ee.taltech.publicapplication.game.model.dto.give_answer;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Accessors(chain = true)
public class GiveAnswerRequest {

    @NotNull
    private Long questionId;

    @NotNull
    private Set<Long> answerIds;

}
