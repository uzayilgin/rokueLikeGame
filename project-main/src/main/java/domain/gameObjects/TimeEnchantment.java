/**
 * Represents a Time Enchantment in the game.
 * A Time Enchantment is a collectible object that can be used by the player to affect gameplay.
 */
package domain.gameObjects;

import domain.gameObjects.Enchantment;
import domain.gameObjects.Player;
import domain.utilities.Constants;

import java.awt.*;

public class TimeEnchantment extends Enchantment {
    private int duration = 0;

    public TimeEnchantment(int x, int y) {
        super(x, y);
    }
    public TimeEnchantment(){

    }

    public void apply() {
    }

    public void use() {
    }

    public void collect(Player player) {
        player.addEnchantment(this);
    }

    @Override
    public void interact() {

    }
    @Override
    public Constants.GameObjectsInHall getType() {
        return Constants.GameObjectsInHall.TIMEENCHANTMENT;
    }
}
