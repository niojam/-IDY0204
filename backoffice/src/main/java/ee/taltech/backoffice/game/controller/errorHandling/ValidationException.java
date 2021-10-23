package ee.taltech.backoffice.game.controller.errorHandling;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ee.taltech.backoffice.game.model.serializer.ValidationExceptionSerializer;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import static ee.taltech.backoffice.game.controller.errorHandling.BadRequest.Code.VALIDATION_EXCEPTION;

@JsonSerialize(using = ValidationExceptionSerializer.class)
public class ValidationException extends BadRequest {

    @Getter @Setter
    private Map<String, String> failedFields;

    public ValidationException(String message) {
        super(VALIDATION_EXCEPTION, message);
    }
}
