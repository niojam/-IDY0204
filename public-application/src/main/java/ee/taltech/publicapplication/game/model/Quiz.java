package ee.taltech.publicapplication.game.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("kahoot.quiz")
@Accessors(chain = true)
public class Quiz {

    @Id
    private Long id;
    private String name;
    private Long authorId;
    private Long firstQuestionId;
    private Long imageId;

}
