package ee.taltech.backoffice.game.controller.errorHandling;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static ee.taltech.backoffice.game.controller.errorHandling.BadRequest.Code.SECURITY_EXCEPTION;
import static ee.taltech.backoffice.game.controller.errorHandling.BadRequest.Code.VALIDATION_EXCEPTION;
import static ee.taltech.backoffice.game.controller.errorHandling.SystemError.Code.SERVER_ERROR;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice(basePackages = "ee.taltech.backoffice.game.controller")
public class ErrorHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({BadRequest.class})
    public BadRequest handleBadRequest(BadRequest e) {
        return e;
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SystemError.class})
    public SystemError handleSystemError(SystemError e) {
        return e;
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler({BadCredentialsException.class, AccessDeniedException.class})
    public BadRequest handleBadCredentials(BadCredentialsException e) {
        return new BadRequest(SECURITY_EXCEPTION, e.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public SystemError handleException(Exception e) {
            return new SystemError(SERVER_ERROR, e.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BadRequest handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(),
                    fieldError.getDefaultMessage());
        }

        ValidationException validationException = new ValidationException(VALIDATION_EXCEPTION.defaultMessage);
        validationException.setFailedFields(errors);
        return validationException;
    }

}
