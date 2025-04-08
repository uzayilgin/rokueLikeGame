/**
 * Represents a Different Wall in the game.
 * A Different Wall is a non-interactive obstacle that blocks movement or visibility in the game.
 */
package domain.gameObjects;

import domain.utilities.Constants;

public class WallDifferent extends GameObject{
    public WallDifferent(int x, int y) {
        super(x, y);
    }

    @Override
    public void interact() {

    }
    @Override
    public String toString() {
        return "Diff Wall";
    }
    @Override
    public Constants.GameObjectsInHall getType() {
        return Constants.GameObjectsInHall.WALLDIFFERENT;
    }
}
