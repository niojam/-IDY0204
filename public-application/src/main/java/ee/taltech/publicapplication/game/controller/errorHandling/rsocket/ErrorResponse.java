package ee.taltech.publicapplication.game.controller.errorHandling.rsocket;

import ee.taltech.publicapplication.game.model.dto.common.GeneralResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ErrorResponse extends GeneralResponse {

    private Exception exception;

}
