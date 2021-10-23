package ee.taltech.publicapplication.game.controller.errorHandling.rsocket;

import ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest;
import ee.taltech.publicapplication.game.controller.errorHandling.exception.SystemError;
import ee.taltech.publicapplication.game.controller.errorHandling.exception.ValidationException;
import ee.taltech.publicapplication.game.model.dto.common.GeneralResponse;
import ee.taltech.publicapplication.game.model.dto.common.ResponseStatus;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;

import static ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest.Code.VALIDATION_EXCEPTION;

public class RSocketExceptionHandler {

    @MessageExceptionHandler
    public Mono<GeneralResponse> handleBadRequest(BadRequest e) {
        return Mono.just(new ErrorResponse()
                .setException(e)
                .setResponseStatus(ResponseStatus.ERROR));
    }

    @MessageExceptionHandler
    public Mono<GeneralResponse> handleSystemError(SystemError e) {
        return Mono.just(new ErrorResponse()
                .setException(e)
                .setResponseStatus(ResponseStatus.ERROR));
    }

    @MessageExceptionHandler
    public Mono<GeneralResponse> handleValidationError(ConstraintViolationException e) {
        ValidationException exception = new ValidationException(VALIDATION_EXCEPTION.getDefaultMessage())
                .setFailedFields(
                        e.getConstraintViolations().stream()
                                .collect(HashMap::new,
                                        (m, c) -> m.put(c.getPropertyPath().toString(), c.getMessage()),
                                        (m, u) -> {
                                        }));
        return Mono.just(new ErrorResponse()
                .setException(exception)
                .setResponseStatus(ResponseStatus.ERROR));
    }

}
