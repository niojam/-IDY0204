package ee.taltech.backoffice.game.model.dto;

import ee.taltech.backoffice.game.model.Quiz;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class QuizDto {

    private Long id;
    private String name;
    private Long authorId;
    private Long imageId;
    private Long firstQuestionId;

    private List<QuestionDto> questions;


    public QuizDto (Quiz quiz) {
        this.id = quiz.getId();
        this.name = quiz.getName();
        this.authorId = quiz.getAuthorId();
        this.imageId = quiz.getImageId();
        this.firstQuestionId = quiz.getFirstQuestionId();
    }
}
