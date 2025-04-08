/**
 * The LifeEnchantment class represents a game object that increases the player's life count.
 * It is an enchantment that players can pick up and use during gameplay.
 */
package domain.gameObjects;

import domain.utilities.Constants;
import technicalServices.logging.LogManager;

public class LifeEnchantment extends Enchantment {
    private static final int LIVES_INCREMENT = 1;

    public LifeEnchantment(int x, int y) {
        super(x, y);
    }
    public LifeEnchantment(){

    }

    @Override
    public void apply() {
    }

    @Override
    public void use() {
    }
    /**
     * Collects the enchantment and applies its effect to the given player.
     *
     * @param player the player collecting the enchantment.
     */
    @Override
    public void collect(Player player) {
        if (player != null) {
            player.incrementLives(LIVES_INCREMENT); // Assuming player has this method
            System.out.println("Player's lives increased by " + LIVES_INCREMENT);
            LogManager.logInfo("Player's lives increased by " + LIVES_INCREMENT + " [from class: LifeEnchantment, method: collect]");
            player.addEnchantment(this);
        }
    }

    @Override
    public void interact() {
    }
    @Override
    public Constants.GameObjectsInHall getType() {
        return Constants.GameObjectsInHall.LIFEENCHANTMENT;
    }
}
