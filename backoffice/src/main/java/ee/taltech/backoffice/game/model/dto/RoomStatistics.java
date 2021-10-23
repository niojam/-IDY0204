package ee.taltech.backoffice.game.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class RoomStatistics {

    private Long roomId;
    private String roomName;

    private Long quizId;
    private String quizName;

    private Long authorId;
    private List<AnswerFrequencyDto> answerFrequencies;
    private List<PlayerStatistics> playerStatistics;

}
