package ee.taltech.backoffice.game.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ee.taltech.backoffice.game.controller.errorHandling.BadRequest;
import ee.taltech.backoffice.game.controller.errorHandling.ValidationException;

import java.io.IOException;

public class ValidationExceptionSerializer extends StdSerializer<ValidationException> {

    public ValidationExceptionSerializer() {
        this(null);
    }

    protected ValidationExceptionSerializer(Class<ValidationException> t) {
        super(t);
    }

    @Override
    public void serialize(ValidationException value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("type", BadRequest.class.getSimpleName());
        gen.writeStringField("code", value.getCode().name());
        gen.writeStringField("message", value.getMessage());
        gen.writeObjectField("failedFields", value.getFailedFields());

        gen.writeEndObject();
    }
}
