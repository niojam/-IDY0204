package ee.taltech.backoffice.game.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.time.Instant;


@Data
@Accessors(chain = true)
public class Room {

    @Id
    private Long id;
    private String pin;
    private String name;
    private RoomStatus status = RoomStatus.REGISTERED;
    private Long currentQuestionId;
    private Instant startedAt;
    private Long quizId;
    private Long playedQuizId;
    private Long authorId;

}
