/**
 * Interface for observing changes in player health.
 * Notifies observers when the player's health changes or the player dies.
 */
package domain.observers;

public interface HealthObserver {
    void onHealthChanged(int newHealth, int oldHealth);
    void isPlayerDead();
}

