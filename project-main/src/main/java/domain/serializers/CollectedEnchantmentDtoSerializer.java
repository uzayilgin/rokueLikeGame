/**
 * Custom serializer for CollectedEnchantmentDto objects.
 * Converts CollectedEnchantmentDto instances into JSON by writing the type field as a string representation
 * of the GameObjectsInHall enum value.
 */
package domain.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import domain.dto.CollectedEnchantmentDto;

import java.io.IOException;

public class CollectedEnchantmentDtoSerializer extends StdSerializer<CollectedEnchantmentDto> {

    public CollectedEnchantmentDtoSerializer() {
        this(null);
    }

    public CollectedEnchantmentDtoSerializer(Class<CollectedEnchantmentDto> t) {
        super(t);
    }

    @Override
    public void serialize(CollectedEnchantmentDto collectedEnchantmentDto, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("type", collectedEnchantmentDto.getType().name());
        jsonGenerator.writeEndObject();
    }
}