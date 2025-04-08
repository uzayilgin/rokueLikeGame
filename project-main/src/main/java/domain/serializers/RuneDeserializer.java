/**
 * A custom deserializer for `Rune` objects using Jackson.
 * Converts JSON data into `Rune` instances by mapping fields like `x`, `y`, and `hallType`.
 */

package domain.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import domain.gameObjects.Rune;
import domain.utilities.Constants;

import java.io.IOException;

public class RuneDeserializer extends StdDeserializer<Rune> {

    public RuneDeserializer() {
        this(null);
    }

    public RuneDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Rune deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String typeStr = node.get("hallType").asText();
        int x = node.get("x").asInt();
        int y = node.get("y").asInt();
        Constants.HallType hallType = Constants.HallType.valueOf(typeStr);
        return new Rune(x, y, hallType);
    }
}