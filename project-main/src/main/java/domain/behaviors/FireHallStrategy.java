/**
 * The FireHallStrategy class defines its behavior specific to the Fire Hall type.
 *
 * This strategy provides methods for setting up the hall, determining its requirements,
 * and retrieving the hall type and a new hall instance.
 */
package domain.behaviors;

import domain.gameObjects.Hall;
import domain.gameObjects.Player;
import domain.utilities.Constants;

public class FireHallStrategy implements HallStrategy {
    Player player;
    /**
     * Creates a FireHallStrategy for the given player.
     *
     * @param player the player associated with this strategy
     */
    public FireHallStrategy(Player player){
        this.player = player;
    }
    @Override
    public void setupHall(Hall hall) {
        System.out.println("Setting up Fire Hall.");
    }

    @Override
    public Constants.HallType getHallType() {
        return Constants.HallType.FIRE;
    }

    @Override
    public int getRequirements() {
        return 17;
    }

    @Override
    public Hall getHall() {
        return new Hall("Fire", this.player, 85, Constants.HallType.FIRE);
    }

}