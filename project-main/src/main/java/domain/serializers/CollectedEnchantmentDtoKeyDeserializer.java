/**
 * Custom key deserializer for CollectedEnchantmentDto objects.
 * Transforms JSON map keys into CollectedEnchantmentDto instances by interpreting the key
 * as a GameObjectsInHall enum value.
 */
package domain.serializers;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import domain.dto.CollectedEnchantmentDto;
import domain.utilities.Constants.GameObjectsInHall;

import java.io.IOException;

public class CollectedEnchantmentDtoKeyDeserializer extends KeyDeserializer {

    @Override
    public CollectedEnchantmentDto deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        GameObjectsInHall type = GameObjectsInHall.valueOf(key);
        CollectedEnchantmentDto dto = new CollectedEnchantmentDto();
        dto.setType(type);
        return dto;
    }
}