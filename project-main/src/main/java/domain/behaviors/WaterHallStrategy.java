/**
 * The WaterHallStrategy class defines its behavior specific to the Water Hall type.
 *
 * This strategy provides methods for setting up the hall, determining its requirements,
 * and retrieving the hall type and a new hall instance.
 */
package domain.behaviors;


import domain.gameObjects.Hall;
import domain.gameObjects.Player;
import domain.utilities.Constants;

public class WaterHallStrategy implements HallStrategy {
    Player player;
    /**
     * Creates a WaterHallStrategy for the given player.
     *
     * @param player the player associated with this strategy
     */
    public WaterHallStrategy(Player player){
        this.player = player;
    }
    @Override
    public void setupHall(Hall hall) {
        System.out.println("Setting up Water Hall.");
    }

    @Override
    public Constants.HallType getHallType() {
        return Constants.HallType.WATER;
    }
    @Override
    public int getRequirements() {
        return 13;
    }

    @Override
    public Hall getHall() {
        return new Hall("Water", this.player, 65, Constants.HallType.WATER);
    }
}