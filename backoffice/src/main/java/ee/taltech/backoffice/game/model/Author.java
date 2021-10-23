package ee.taltech.backoffice.game.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

@Data
@Accessors(chain = true)
public class Author {

    @Id
    private Long id;
    private String oid;
    private String username;

}
