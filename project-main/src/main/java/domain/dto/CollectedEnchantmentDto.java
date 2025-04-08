/**
 * Represents a Data Transfer Object for a collected enchantment in the game.
 *
 * This class encapsulates information about the type of enchantment collected
 * by the player and provides functionality to compare and hash these objects.
 */
package domain.dto;

import domain.utilities.Constants;

import java.util.Objects;

public class CollectedEnchantmentDto {

    private Constants.GameObjectsInHall type;


    public Constants.GameObjectsInHall getType() {
        return type;
    }

    public void setType(Constants.GameObjectsInHall type) {
        this.type = type;
    }
    /**
     * Compares this object with another for equality.
     *
     * Two CollectedEnchantmentDto objects are considered equal if their types are the same.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectedEnchantmentDto that = (CollectedEnchantmentDto) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
