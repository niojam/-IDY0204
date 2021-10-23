package ee.taltech.backoffice.game.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Image {

    @Id
    private Long id;
    private String fileName;
    private String contentType;
    private byte[] content;

}
