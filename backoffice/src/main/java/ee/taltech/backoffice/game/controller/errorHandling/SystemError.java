package ee.taltech.backoffice.game.controller.errorHandling;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ee.taltech.backoffice.game.model.serializer.SystemErrorSerializer;
import lombok.Getter;

@JsonSerialize(using = SystemErrorSerializer.class)
public class SystemError extends RuntimeException {

    @Getter
    final Code code;

    @Getter
    final String message;

    public enum Code {
        SERVER_ERROR("Unexpected server error.");

        String defaultMessage;

        Code(String defaultMessage) {
            this.defaultMessage = defaultMessage;
        }
    }

    public SystemError(Code code) {
        this.code = code;
        this.message = code.defaultMessage;
    }

    public SystemError(Code code, String message) {
        this.code = code;
        this.message = message;
    }
}
