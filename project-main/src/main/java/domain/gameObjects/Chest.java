/**
 * The Chest class represents a chest object in the game that can hold items or interact with the player.
 */
package domain.gameObjects;

import domain.utilities.Constants;

public class Chest extends GameObject {

    public Chest(int x, int y) {
        super(x, y);
    }

    @Override
    public String toString() {
        return "Chest";
    }
    @Override
    public void interact() {

    }
    @Override
    public Constants.GameObjectsInHall getType() {
        return Constants.GameObjectsInHall.CHEST;
    }
}
