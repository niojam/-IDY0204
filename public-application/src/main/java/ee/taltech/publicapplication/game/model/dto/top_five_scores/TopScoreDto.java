package ee.taltech.publicapplication.game.model.dto.top_five_scores;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TopScoreDto {

    private String username;
    private Long totalScore;
    private Long reward;

}
