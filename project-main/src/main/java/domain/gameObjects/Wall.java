/**
 * Represents a Wall in the game.
 * A Wall is a non-interactive obstacle that blocks movement or visibility in the game.
 */

package domain.gameObjects;

import domain.utilities.Constants;

public class Wall extends GameObject {

    public Wall(int x, int y) {
        super(x, y);
    }

    @Override
    public void interact() {

    }
    @Override
    public String toString() {
        return "Wall";
    }
    @Override
    public Constants.GameObjectsInHall getType() {
        return Constants.GameObjectsInHall.WALL;
    }
}
