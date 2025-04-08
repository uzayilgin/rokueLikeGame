/**
 * The Enchantment class serves as an abstract base for all enchantments in the game.
 * Enchantments can either be located in the game hall or collected by the player.
 */
package domain.gameObjects;

import domain.utilities.Constants;

import java.awt.*;

public abstract class Enchantment  extends GameObject{
    public int duration = 0;
    public int positionX;
    public int positionY;
    public Constants.GameObjectsInHall type;
// here enchantments not necessarily have a position, if they are collected by the player, they will be stored in the player object
    //thats why i deleted its extension from the GameObject class
    public Enchantment(int x, int y) {
        super(x, y);
    }
    // this is for the save part, when saving the collected enchantments of the player we will use this constructor
    //when enchantment is in the hall it will have a position and will be stored in the hall's hashmap
    public Enchantment(){
    }

    public abstract void apply();
    public abstract void use();
    public abstract void collect(Player player);

}
