package ee.taltech.backoffice.game.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Accessors(chain = true)
@Table("answer_frequency")
public class AnswerFrequency {

    @Id
    private Long id;

    private Long answerId;

    private Long frequency;

    private Long questionId;

    private Long roomId;
}
