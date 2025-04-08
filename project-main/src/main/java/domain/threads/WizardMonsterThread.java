/**
 * WizardMonsterThread manages the threaded behavior of a WizardMonster.
 * It dynamically updates the monster's strategy based on the game's remaining time and current state.
 * The thread ensures the WizardMonster executes its behavior while responding to game events like pausing or game over.
 */

package domain.threads;

import java.awt.*;
import java.util.Random;

import domain.behaviors.WizardStrategy;
import domain.gameCore.GameState;
import domain.gameObjects.WizardMonster;
import domain.behaviors.DoNothingStrategy;
import domain.behaviors.MovePlayerStrategy;
import domain.behaviors.TeleportRuneStrategy;
import domain.gameObjects.Hall;

public class WizardMonsterThread extends MonsterThread {
    private WizardStrategy currentBehavior;
    private Hall hall;

    public WizardMonsterThread(WizardMonster monster, GameState model) {
        super(monster, model);
        this.hall = model.getHall();
        updateBehavior(hall.getTimeRemaining());
    }
    // draw method can be removed if not needed.
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(x * 50, y * 50, 50, 50);
    }
    /**
     * Executes the current behavior strategy for the WizardMonster.
     * The behavior is dynamically updated based on the remaining time in the game.
     */

    @Override
    public void act() {
        if (!hall.getMonsters().contains(monster)) {
            return;
        }
        if (currentBehavior != null) {
            currentBehavior.execute((WizardMonster) monster, hall);
        }
    }
    /**
     * The main execution loop for the WizardMonsterThread.
     * Continuously updates the WizardMonster's behavior and ensures proper
     * thread management during game pauses or game over events.
     */

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            while (model.isPaused()) {
                if (!hall.getMonsters().contains(monster)) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            try {
                int timeRemaining = model.getTimer().getTimeRemaining();
                updateBehavior(timeRemaining);
                act();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            if (model.isGameOver()) {
                break;
            }
        }
    }
    /**
     * Updates the behavior strategy for the WizardMonster based on the game's remaining time.
     *
     * @param timeRemaining The remaining time in the game, used to determine the appropriate behavior.
     *                      - Less than 30% remaining: MovePlayerStrategy.
     *                      - More than 70% remaining: TeleportRuneStrategy.
     *                      - Otherwise: DoNothingStrategy.
     */

    private void updateBehavior(int timeRemaining) {;
        int totalTime = model.getTotalTime();
        double percentageRemaining = (timeRemaining / (double) totalTime) * 100;
        if (!hall.getMonsters().contains(monster)) {
            currentBehavior = null;
            return;
        }
        WizardStrategy newStrategy;
        if (percentageRemaining < 30) {
            newStrategy = new MovePlayerStrategy(model.getController().getPlayerController());
        } else if (percentageRemaining > 70) {
            newStrategy = new TeleportRuneStrategy(model);
        } else {
            newStrategy = new DoNothingStrategy();
        }
        if (currentBehavior != null
                && !currentBehavior.getClass().equals(newStrategy.getClass())
                && currentBehavior instanceof TeleportRuneStrategy) {
            ((TeleportRuneStrategy) currentBehavior).stop();
        }
        if (currentBehavior != null
                && !currentBehavior.getClass().equals(newStrategy.getClass())
                && currentBehavior instanceof MovePlayerStrategy) {
            ((MovePlayerStrategy) currentBehavior).stop();
        }
        if (currentBehavior != null
                && !currentBehavior.getClass().equals(newStrategy.getClass())
                && currentBehavior instanceof DoNothingStrategy) {
            ((DoNothingStrategy) currentBehavior).stop();
        }

        if (currentBehavior == null
                || !currentBehavior.getClass().equals(newStrategy.getClass())) {
            currentBehavior = newStrategy;
        }
    }
}

