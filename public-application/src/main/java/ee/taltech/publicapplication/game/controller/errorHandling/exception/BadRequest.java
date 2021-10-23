package ee.taltech.publicapplication.game.controller.errorHandling.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ee.taltech.publicapplication.game.model.serializer.BadRequestSerializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonSerialize(using = BadRequestSerializer.class)
public class BadRequest extends RuntimeException {

    private final Code code;
    private final String message;

    @Getter
    @RequiredArgsConstructor
    public enum Code {
        SECURITY_EXCEPTION("Access denied"),
        VALIDATION_EXCEPTION("Object field validation failed."),
        BUSINESS_LOGIC_EXCEPTION("Unexpected error in business logic"),
        INVALID_ARGUMENT_EXCEPTION("Some of the fields have incorrect values.");

        private final String defaultMessage;
    }

}
