package ee.taltech.publicapplication.game.controller.errorHandling.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ee.taltech.publicapplication.game.model.serializer.ValidationExceptionSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

import static ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest.Code.VALIDATION_EXCEPTION;

@Getter
@Setter
@Accessors(chain = true)
@JsonSerialize(using = ValidationExceptionSerializer.class)
public class ValidationException extends BadRequest {

    private Map<String, String> failedFields;

    public ValidationException(String message) {
        super(VALIDATION_EXCEPTION, message);
    }

}
