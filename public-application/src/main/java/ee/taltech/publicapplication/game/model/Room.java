package ee.taltech.publicapplication.game.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Table("kahoot.room")
@Accessors(chain = true)
public class Room {

    @Id
    private Long id;
    private Long quizId;
    private Long playedQuizId;

    private Long authorId;
    private Long currentQuestionId;
    private Instant startedAt;

    private String pin;
    private String name;

    private RoomStatus status;

}
