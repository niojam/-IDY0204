package ee.taltech.publicapplication.game.model.dto.top_five_scores;

import ee.taltech.publicapplication.game.model.dto.common.GeneralResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class TopScoresResponse extends GeneralResponse {

    private List<TopScoreDto> topScores;

}
