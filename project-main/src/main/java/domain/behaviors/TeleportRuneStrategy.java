/**
 * The TeleportRuneStrategy class defines a behavior for a WizardMonster
 * to teleport the rune in each 3 seconds.
 *
 * This strategy operates on a separate thread, periodically teleporting the rune
 * unless the game is paused or the time is over.
 */
package domain.behaviors;

import domain.gameCore.GameState;
import domain.gameObjects.Hall;
import domain.gameObjects.WizardMonster;

public class TeleportRuneStrategy implements WizardStrategy {
    private GameState model;
    private Thread teleportThread;
    private boolean isRunning = false;
    /**
     * Creates a TeleportRuneStrategy with the specified game state model.
     *
     * @param model the current game state model
     */
    public TeleportRuneStrategy(GameState model) {
        this.model = model;
    }
    /**
     * Executes the teleportation strategy for the WizardMonster and Hall.
     *
     * Starts a thread that periodically teleports the rune in the hall, and it ensures that
     * it pauses when the game is paused and stops when the time is over.
     *
     * @param monster the WizardMonster executing the strategy
     * @param hall the Hall where the strategy is applied
     */
    @Override
    public void execute(WizardMonster monster, Hall hall) {
        if (!isRunning) {
            isRunning = true;
            teleportThread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        while (model.isPaused()) {
                            Thread.sleep(1000);
                        }
                        Thread.sleep(3000);
                        if (hall.getTimeRemaining() > 0) {
                            hall.teleportRune();
                            System.out.println("WizardMonster teleported the rune.");
                        } else {
                            System.out.println("Time is over. Stopping behavior.");
                            break;
                        }
                    } catch (InterruptedException e) {
                        System.out.println("TeleportRuneBehavior interrupted.");
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
            teleportThread.start();
        }
    }
    /**
     * Stops the teleportation thread if it is running.
     *
     * Interrupts the active thread and resets the state.
     */
    @Override
    public void stop() {
        if (teleportThread != null && teleportThread.isAlive()) {
            teleportThread.interrupt();
            try {
                teleportThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        teleportThread = null;
        isRunning = false;
    }
}