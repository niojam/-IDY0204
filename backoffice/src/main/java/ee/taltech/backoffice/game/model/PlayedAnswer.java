package ee.taltech.backoffice.game.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("kahoot.played_answer")
@Accessors(chain = true)
public class PlayedAnswer {

    @Id
    private Long id;
    private String text;
    private Long questionId;
    private Boolean isCorrect;
    private Long parentAnswerId;

}
