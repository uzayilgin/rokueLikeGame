/**
 * A custom key deserializer for `Point` objects using Jackson.
 * Converts string representations of `Point` (used as keys in JSON) into Java `Point` instances.
 */

package domain.serializers;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.awt.*;
import java.io.IOException;

public class PointKeyDeserializer extends KeyDeserializer {

    @Override
    public Point deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        String[] parts = key.replace("java.awt.Point[x=", "").replace("]", "").split(",y=");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        return new Point(x, y);
    }
}
