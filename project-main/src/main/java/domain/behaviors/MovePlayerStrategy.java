/**
 * The MovePlayerStrategy class defines a behavior for a WizardMonster to move the player
 * to a random empty location in the hall and then disappear.
 *
 * This strategy involves relocating the player, removing the WizardMonster from the hall,
 * and notifying listeners.
 */
package domain.behaviors;

import domain.gameObjects.Hall;
import domain.gameObjects.Player;
import domain.gameObjects.WizardMonster;
import domain.controllers.PlayerController;

import java.awt.Point;

public class MovePlayerStrategy implements WizardStrategy {
    private boolean hasMovedPlayer = false;
    private Thread disappearanceThread;
    private PlayerController playerController;
    /**
     * Creates a MovePlayerStrategy with the specified PlayerController.
     *
     * @param playerController the controller managing player actions
     */
    public MovePlayerStrategy(PlayerController playerController) {
        this.playerController = playerController;
    }
    /**
     * Executes the strategy for the given WizardMonster and Hall.
     *
     * Moves the player to a random empty position in the hall, removes the WizardMonster,
     * and notifies listeners of the updates.
     *
     * @param monster the WizardMonster executing the strategy
     * @param hall the Hall where the strategy is applied
     */
    @Override
    public void execute(WizardMonster monster, Hall hall) {
        if (!monster.hasAlreadyMovedPlayer()) {
            monster.setHasMovedPlayer(true);
            disappearanceThread = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                Player player = hall.getPlayer();
                Point randomEmptyLocation = hall.getRandomEmptyPosition();
                Point currentPosition = player.getPosition();
                if (randomEmptyLocation != null) {
                    player.setPosition(randomEmptyLocation);
                    System.out.println("Player moved to a new location: " + randomEmptyLocation);
                }
                hall.removeMonster(monster);
                hall.removeObject(monster.getPosition());
                hall.moveObject(currentPosition, randomEmptyLocation);
                hall.notifyListeners();
                System.out.println("movePlayerStrategy is executed (WizardMonster disappeared)");
            });
            disappearanceThread.start();
        }
    }
    /**
     * Stops the thread if it is running.
     *
     * Interrupts the active thread and resets the state.
     */
    @Override
    public void stop() {
        if (disappearanceThread != null && disappearanceThread.isAlive()) {
            disappearanceThread.interrupt();
            try {
                disappearanceThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        disappearanceThread = null;
        hasMovedPlayer = false;
    }
}

