/**
 * The AirHallStrategy class defines its behavior specific to the Air Hall type.
 *
 * It provides functionality for setting up the hall, determining requirements,
 * and retrieving the hall type and a new hall instance.
 */

package domain.behaviors;

import domain.gameObjects.Hall;
import domain.gameObjects.Player;
import domain.utilities.Constants;
import ui.swing.AirHallScreen;

public class AirHallStrategy implements HallStrategy{
    Player player;
    /**
     * Creates an AirHallStrategy for the given player.
     *
     * @param player the player associated with this strategy
     */
    public AirHallStrategy(Player player){
        this.player = player;
    }
    @Override
    public void setupHall(Hall hall) {
        System.out.println("Setting up Air Hall.");
    }


    @Override
    public Constants.HallType getHallType() {
        return Constants.HallType.AIR;
    }
    @Override
    public int getRequirements() {
        return 9;
    }

    @Override
    public Hall getHall() {
        return new Hall("Air", this.player, 45, Constants.HallType.AIR);
    }
}