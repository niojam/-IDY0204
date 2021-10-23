package ee.taltech.publicapplication.game.model.dto;

import ee.taltech.publicapplication.game.model.dto.common.GeneralResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ScoreDto extends GeneralResponse {

    private Long totalScore;
    private Long reward;

}
