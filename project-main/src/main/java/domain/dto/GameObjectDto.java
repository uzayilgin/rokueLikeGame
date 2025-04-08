/**
 * Represents a Data Transfer Object for a game object in the game.
 *
 * This class stores the position and type of a game object, providing
 * methods to access and modify these properties.
 */
package domain.dto;

import domain.utilities.Constants;

import java.io.Serializable;

public class GameObjectDto implements Serializable {
    private int x;
    private int y;
    private Constants.GameObjectsInHall type;

    public int getX() {
        return x;
    }
    public Constants.GameObjectsInHall getType() {
        return type;
    }
    public void setType(Constants.GameObjectsInHall type) {
        this.type = type;
    }

    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
}
