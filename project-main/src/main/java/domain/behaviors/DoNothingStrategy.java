/**
 * The DoNothingStrategy class implements behavior for a WizardMonster
 * that disappears after a delay without performing any additional actions.
 *
 * This strategy triggers a disappearance process where the monster is removed
 * from the hall after 2 seconds and listeners are notified of the change.
 */

package domain.behaviors;

import domain.gameObjects.Hall;
import domain.gameObjects.WizardMonster;

public class DoNothingStrategy implements WizardStrategy {

    private boolean isRunning = false;
    private Thread disappearanceThread;
    /**
     * Executes the strategy for the given WizardMonster and Hall.
     *
     * Starts a thread which waits for 2 seconds and then removes the monster
     * from the hall, notifying listeners of the change.
     *
     * @param monster the WizardMonster on which the strategy is executed
     * @param hall the Hall where the monster exists
     */
    @Override
    public void execute(WizardMonster monster, Hall hall) {
        if (!isRunning) {
            isRunning = true;

            disappearanceThread = new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                hall.removeObject(monster.getPosition());
                hall.removeMonster(monster);
                hall.notifyListeners();
                System.out.println("DoNothingStrategy executed (WizardMonster disappeared after 2s)");
            });

            disappearanceThread.start();
        }
    }
    /**
     * Stops the thread.
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
        isRunning = false;
    }
}
