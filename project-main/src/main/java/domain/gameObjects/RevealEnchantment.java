/**
 * Represents a "Reveal" enchantment in the game.
 * This enchantment can be collected by the player and added to their inventory.
 */
package domain.gameObjects;

import domain.utilities.Constants;

import java.awt.*;

public class RevealEnchantment extends Enchantment {
    private int duration = 0;

    public RevealEnchantment(int x, int y) {
        super(x, y);
    }
    public RevealEnchantment(){

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
        return Constants.GameObjectsInHall.REVEALENCHANTMENT;
    }
}
