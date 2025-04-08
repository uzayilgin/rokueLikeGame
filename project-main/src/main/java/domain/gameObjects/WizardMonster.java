/**
 * Represents a Wizard Monster in the game.
 * The Wizard Monster does not attack or move directly but can execute specific behaviors.
 * It uses the Strategy pattern to delegate its behavior dynamically.
 */
package domain.gameObjects;

import domain.utilities.Constants;
import technicalServices.logging.LogManager;
import domain.behaviors.WizardStrategy;
import domain.behaviors.DoNothingStrategy;
import domain.behaviors.MovePlayerStrategy;
import domain.behaviors.TeleportRuneStrategy;
import java.awt.*;
import java.util.ArrayList;

public class WizardMonster extends Monster{
    private WizardStrategy behavior;
    private boolean hasMovedPlayer = false;

    public boolean hasAlreadyMovedPlayer() { return hasMovedPlayer; }
    public void setHasMovedPlayer(boolean moved) { this.hasMovedPlayer = moved; }

    public WizardMonster(int x, int y) {
        super(x, y);
        this.behavior = new DoNothingStrategy();
    }

    public WizardStrategy getBehavior() {
        return behavior;
    }

    public void setBehavior(WizardStrategy behavior) {
        this.behavior = behavior;
    }
    public void executeBehavior(Hall hall) {
        if (behavior != null) {
            behavior.execute(this, hall);
        }
    }
    // information expert pattern should be used, who knows the empty locations in the grid?
    @Override
    public void attack(Player player) {
        LogManager.logInfo("Wizard monster does not attacking");
    }

    @Override
    public void move(ArrayList<Point> emptyLocations) {
        LogManager.logInfo("Wizard monster does not move");
    }

    @Override
    public void interact() {
        LogManager.logInfo("Wizard monster is interacting with the player. [from class: WizardMonster, method: interact]");
    }

    @Override
    public void render(Graphics g) {
        // Draw the fighter monster
        g.setColor(Color.RED); // Example color for the fighter monster
        Point pos = getPosition();
        g.fillRect(pos.x * 20, pos.y * 20, 20, 20); // Example rendering as a 20x20 square
    }
    @Override
    public Constants.GameObjectsInHall getType() {
        return Constants.GameObjectsInHall.WIZARD;
    }
}
