package ee.taltech.publicapplication.game.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ee.taltech.publicapplication.game.controller.errorHandling.exception.SystemError;

import java.io.IOException;

public class SystemErrorSerializer extends StdSerializer<SystemError> {

    public SystemErrorSerializer() {
        this(null);
    }

    protected SystemErrorSerializer(Class<SystemError> t) {
        super(t);
    }

    @Override
    public void serialize(SystemError value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("type", SystemError.class.getSimpleName());
        gen.writeStringField("code", value.getCode().name());
        gen.writeStringField("message", value.getMessage());

        gen.writeEndObject();
    }
}
