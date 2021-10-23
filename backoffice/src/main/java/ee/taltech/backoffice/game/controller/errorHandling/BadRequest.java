package ee.taltech.backoffice.game.controller.errorHandling;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ee.taltech.backoffice.game.model.serializer.BadRequestSerializer;
import lombok.Getter;

@JsonSerialize(using = BadRequestSerializer.class)
public class BadRequest extends RuntimeException {

    @Getter
    private final Code code;

    @Getter
    private final String message;

    public enum Code {
        SECURITY_EXCEPTION("Access denied"),
        VALIDATION_EXCEPTION("Object field validation failed."),
        INVALID_ARGUMENT_EXCEPTION("Some of the fields have incorrect values."),
        BAD_REQUEST_EXCEPTION("Request cannot be processed");

        String defaultMessage;

        Code(String defaultMessage) {
            this.defaultMessage = defaultMessage;
        }
    }


    public BadRequest(Code code) {
        this.code = code;
        this.message = code.defaultMessage;
    }

    public BadRequest(Code code, String message) {
        this.code = code;
        this.message = message;
    }
}
