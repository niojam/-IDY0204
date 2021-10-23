package ee.taltech.publicapplication.game.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Accessors(chain = true)
@Table("kahoot.question")
public class Question {

    @Id
    private Long id;
    private Long quizId;
    private Long imageId;
    private String text;
    private String title;
    private Long timer;
    private Long reward;
    private Long nextQuestionId;
    private QuestionType questionType;
    private TimeAlgorithm timeAlgorithm;

}
