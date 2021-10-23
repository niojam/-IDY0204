package ee.taltech.backoffice.game.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
public class RoomDto {

    private Long id;
    private String roomName;
    private String quizName;
    private Long playedQuizId;
    private Long authorId;
    private Instant startedAt;

}
