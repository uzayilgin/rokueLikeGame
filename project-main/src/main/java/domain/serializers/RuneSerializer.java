/**
 * A custom serializer for `Rune` objects using Jackson.
 * Converts `Rune` instances into JSON format, including fields like `hallType`, `x`, and `y`.
 */
package domain.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import domain.gameObjects.Rune;

import java.io.IOException;

public class RuneSerializer extends StdSerializer<Rune> {

    public RuneSerializer() {
        this(null);
    }

    public RuneSerializer(Class<Rune> t) {
        super(t);
    }

    @Override
    public void serialize(Rune rune, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("hallType", rune.getRuneType().name());
        gen.writeNumberField("x", rune.getX());
        gen.writeNumberField("y", rune.getY());
        gen.writeEndObject();
    }
}