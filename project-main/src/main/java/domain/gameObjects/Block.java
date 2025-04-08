/**
 * The Block class represents an immovable block object in the game.
 * Blocks are static objects that cannot interact or be interacted with by other objects.
 */
package domain.gameObjects;

import domain.utilities.Constants;

public class Block extends GameObject{
    public Block(int x, int y) {
        super(x, y);
    }

    @Override
    public void interact() {

    }
    @Override
    public String toString() {
        return "Block";
    }
    @Override
    public Constants.GameObjectsInHall getType() {
        return Constants.GameObjectsInHall.BLOCK;
    }
}
