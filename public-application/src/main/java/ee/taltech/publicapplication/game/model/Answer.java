package ee.taltech.publicapplication.game.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Accessors(chain = true)
@Table("kahoot.answer")
public class Answer {

    @Id
    private Long id;
    private String text;
    private Long questionId;
    private Boolean isCorrect;

}
