package ee.taltech.publicapplication.game.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("kahoot.played_quiz")
@Accessors(chain = true)
public class PlayedQuiz {

    @Id
    private Long id;
    private String name;
    private Long authorId;

}
