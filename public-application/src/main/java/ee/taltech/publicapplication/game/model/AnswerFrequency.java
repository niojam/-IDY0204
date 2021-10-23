package ee.taltech.publicapplication.game.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.concurrent.atomic.AtomicLong;

@Data
@Accessors(chain = true)
@Table("kahoot.answer_frequency")
public class AnswerFrequency {

    @Id
    private Long id;
    private Long roomId;
    private Long answerId;
    private Long questionId;
    private AtomicLong frequency;

    public void incrementFrequency() {
        this.frequency.incrementAndGet();
    }

}
