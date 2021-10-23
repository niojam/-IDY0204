package ee.taltech.publicapplication.game.controller.errorHandling.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ee.taltech.publicapplication.game.model.serializer.SystemErrorSerializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonSerialize(using = SystemErrorSerializer.class)
public class SystemError extends RuntimeException {

    private final Code code;
    private final String message;

    @Getter
    @RequiredArgsConstructor
    public enum Code {
        SERVER_ERROR("Unexpected server error.");

        private final String defaultMessage;
    }

}
