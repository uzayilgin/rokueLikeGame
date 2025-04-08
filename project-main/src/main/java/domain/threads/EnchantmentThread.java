/**
 * EnchantmentThread is a runnable task responsible for managing the lifecycle of an enchantment in the game.
 * The enchantment remains active for a specified duration before disappearing from the game state.
 * It also ensures compatibility with game pauses.
 */
package domain.threads;

import domain.gameCore.GameState;
import domain.gameObjects.Enchantment;

public class EnchantmentThread implements Runnable{
    protected int x, y;
    protected boolean alive = true;
    protected GameState model;
    protected Enchantment enchantment;
    protected int duration = 6;
    public EnchantmentThread(Enchantment enchantment, GameState model) {
        this.enchantment = enchantment;
        this.model = model;
    }
    public EnchantmentThread(Enchantment enchantment, GameState model, int duration) {
        this.enchantment = enchantment;
        this.model = model;
        this.duration = duration;
    }
    private boolean isAlive() {
        if (duration == 0) {
            alive = false;
        }
        return alive;
    }

    // this function will be used to disappear the enchantment after gathering
    public void kill() {
        alive = false;
    }
    /**
     * The main execution logic for the thread.
     * Places the enchantment into the game state, manages its lifecycle, and removes it upon expiration.
     */
    @Override
    public void run() {
        model.getHall().getGameObjects().put(enchantment.getPosition(), enchantment);
        while (isAlive()) {
            while (model.isPaused()) {
                // do nothing
            }
            try {
                Thread.sleep(1000);
                duration--;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        model.getHall().removeObject(enchantment.getPosition());

    }
}
