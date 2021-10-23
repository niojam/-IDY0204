package ee.taltech.publicapplication.game.model.dto.common;

import lombok.Data;
import lombok.experimental.Accessors;

import static ee.taltech.publicapplication.game.model.dto.common.ResponseStatus.OK;

@Data
@Accessors(chain = true)
public class GeneralResponse {
    
    private ResponseStatus responseStatus = OK;
    
}
