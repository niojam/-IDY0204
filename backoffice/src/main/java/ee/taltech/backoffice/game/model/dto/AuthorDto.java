package ee.taltech.backoffice.game.model.dto;


import ee.taltech.backoffice.game.model.Author;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthorDto {

    private Long id;
    private String username;

    public AuthorDto (Author author) {
        this.id = author.getId();
        this.username = author.getUsername();
    }

}
