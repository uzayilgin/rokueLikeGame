/**
 * Custom deserializer for CollectedEnchantmentDto objects.
 * Converts JSON strings into CollectedEnchantmentDto instances by mapping the string
 * to the appropriate GameObjectsInHall enum value.
 */
package domain.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import domain.dto.CollectedEnchantmentDto;
import domain.utilities.Constants.GameObjectsInHall;

import java.io.IOException;

public class CollectedEnchantmentDtoDeserializer extends JsonDeserializer<CollectedEnchantmentDto> {

    @Override
    public CollectedEnchantmentDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String type = jsonParser.getText();
        CollectedEnchantmentDto dto = new CollectedEnchantmentDto();
        try {
            dto.setType(GameObjectsInHall.valueOf(type));
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid type for GameObjectsInHall enum: " + type, e);
        }
        return dto;
    }
}