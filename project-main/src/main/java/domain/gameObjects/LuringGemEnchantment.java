/**
 * The LuringGemEnchantment class represents a unique enchantment that can attract monsters when activated.
 * This enchantment is implemented as a singleton, ensuring only one instance exists during the game.
 */
package domain.gameObjects;

import domain.utilities.Constants;

import java.awt.*;

public class LuringGemEnchantment extends Enchantment {
    private int isActive = 0; // 0 for inactive, 1 for active

    public LuringGemEnchantment(int x, int y) {
        super(x, y);
    }
    public LuringGemEnchantment(){

    }

    public void apply() {
    }

    public void use() {
    }

    public void collect(Player player) {
        this.isActive = 0;
        player.addEnchantment(this);
    }

    @Override
    public void interact() {

    }
    @Override
    public Constants.GameObjectsInHall getType() {
        return Constants.GameObjectsInHall.LURINGENCHANTMENT;
    }

    private static LuringGemEnchantment instance;
    public static synchronized LuringGemEnchantment getInstance(int x, int y) {
        if (instance == null) {
            instance = new LuringGemEnchantment(x, y);
        }
        return instance;
    }

    public int getIsActive() {
        return isActive;
    }
    public void setIsActive(int isActive, Point position) {
        this.setPosition(position.x, position.y);
        this.isActive = isActive;
    }
}
