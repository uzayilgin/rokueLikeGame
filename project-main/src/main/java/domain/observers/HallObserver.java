/**
 * Interface for observing changes in the hall, such as the addition or replacement of objects,
 * and checking if the player is dead.
 */
package domain.observers;

public interface HallObserver {
    void isPlayerDead();

    public void onObjectAdded();
    public void onObjectReplaced();
}
