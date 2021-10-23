package ee.taltech.backoffice.security.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UsernameAndPassword {

    private String username;
    private String password;

}
