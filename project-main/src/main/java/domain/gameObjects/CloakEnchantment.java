/**
 * The CloakEnchantment class represents an enchantment that grants the player temporary invisibility or other special abilities.
 */
package domain.gameObjects;

import domain.utilities.Constants;

public class CloakEnchantment extends Enchantment {
    private int duration = 0;

    public CloakEnchantment(int x, int y) {
        super(x, y);
    }
    public CloakEnchantment(){

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
        return Constants.GameObjectsInHall.CLOAKENCHANTMENT;
    }
}
