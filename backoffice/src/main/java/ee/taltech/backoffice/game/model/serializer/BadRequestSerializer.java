package ee.taltech.backoffice.game.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ee.taltech.backoffice.game.controller.errorHandling.BadRequest;

import java.io.IOException;

public class BadRequestSerializer extends StdSerializer<BadRequest> {

    public BadRequestSerializer() {
        this(null);
    }

    protected BadRequestSerializer(Class<BadRequest> t) {
        super(t);
    }

    @Override
    public void serialize(BadRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("type", BadRequest.class.getSimpleName());
        gen.writeStringField("code", value.getCode().name());
        gen.writeStringField("message", value.getMessage());

        gen.writeEndObject();
    }
}
