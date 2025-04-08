/**
 * Interface for observing monster-related activities in the game.
 * Notifies observers of events like spawning, removal, movement, attacks, and interactions.
 */
package domain.observers;

import domain.gameObjects.Monster;

public interface MonsterObserver {
    void onMonsterSpawned(Monster monster);
    void onMonsterRemoved(Monster monster);
    void onMonsterMoved(Monster monster);
    void onMonsterAttacked(Monster monster);
    void onMonsterInteracted(Monster monster);
}
