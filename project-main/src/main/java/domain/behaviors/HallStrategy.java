/**
 * The HallStrategy defines an interface for strategies associated with different Hall types.
 *
 * Implementing classes should provide specific behaviors for setting up a hall,
 * determining its type and requirements, and creating a new hall instance.
 */
package domain.behaviors;

import domain.gameObjects.Hall;
import domain.utilities.Constants;

public interface HallStrategy {
    void setupHall(Hall hall);
    public Constants.HallType getHallType();
    int getRequirements();
    public Hall getHall();
}