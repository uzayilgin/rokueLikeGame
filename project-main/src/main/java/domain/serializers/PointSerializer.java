/**
 * A custom serializer for `Point` objects using Jackson.
 * Converts Java `Point` instances into JSON objects with `x` and `y` fields.
 */
package domain.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.awt.Point;
import java.io.IOException;

public class PointSerializer extends StdSerializer<Point> {

    public PointSerializer() {
        this(null);
    }

    public PointSerializer(Class<Point> t) {
        super(t);
    }

    @Override
    public void serialize(Point point, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("x", point.x);
        jsonGenerator.writeNumberField("y", point.y);
        jsonGenerator.writeEndObject();
    }
}
