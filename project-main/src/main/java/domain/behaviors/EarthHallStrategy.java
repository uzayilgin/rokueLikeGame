/**
 * The EarthHallStrategy class defines its behavior specific to the Earth Hall type.
 *
 * This strategy provides methods for setting up the hall, determining its requirements,
 * and retrieving the hall type and a new hall instance.
 */

package domain.behaviors;

import domain.gameObjects.Hall;
import domain.gameObjects.Player;
import domain.utilities.Constants;

public class EarthHallStrategy implements HallStrategy {
    Player player;
    /**
     * Creates an EarthHallStrategy for the given player.
     *
     * @param player the player associated with this strategy
     */
    public EarthHallStrategy(Player player){
        this.player = player;
    }
    @Override
    public void setupHall(Hall hall) {
        System.out.println("Setting up Earth Hall.");
        // Add earth-specific objects or configurations
    }

    @Override
    public Constants.HallType getHallType() {
        return Constants.HallType.EARTH;
    }
    @Override
    public int getRequirements() {
        return 6;
    }

    @Override
    public Hall getHall() {
        return new Hall("Earth", this.player, 300, Constants.HallType.EARTH);
    }

}