package ee.taltech.publicapplication.game.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("kahoot.played_question")
@Accessors(chain = true)
public class PlayedQuestion {

    @Id
    private Long id;
    private Long quizId;
    private String text;
    private String title;
    private Long timer;
    private Long reward;
    private QuestionType questionType;
    private TimeAlgorithm timeAlgorithm;
    private Long parentQuestionId;

}
