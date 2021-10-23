package ee.taltech.backoffice.game.model;

import ee.taltech.backoffice.game.model.dto.QuizDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Quiz {

    @Id
    private Long id;

    private String name;

    private Long authorId;

    private Long firstQuestionId;

    private Long imageId;

    public Quiz(QuizDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.authorId = dto.getAuthorId();
        this.firstQuestionId = dto.getFirstQuestionId();
        this.firstQuestionId = dto.getImageId();
    }

}
