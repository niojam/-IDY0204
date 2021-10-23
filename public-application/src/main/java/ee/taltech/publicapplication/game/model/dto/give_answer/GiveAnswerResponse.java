package ee.taltech.publicapplication.game.model.dto.give_answer;

import ee.taltech.publicapplication.game.model.dto.ScoreDto;
import ee.taltech.publicapplication.game.model.dto.common.GeneralResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GiveAnswerResponse extends GeneralResponse {

    private ScoreDto scoreDto;

}
