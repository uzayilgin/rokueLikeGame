/**
 * Interface for observing changes in the player's inventory.
 * Provides updates when the inventory is modified with a new set of enchantments and their counts.
 */
package domain.observers;

import domain.gameObjects.Enchantment;

import java.util.Map;

public interface InventoryObserver {
    /**
     * This method will be called when the player's inventory is changed.
     *
     * @param newInventory The updated map of enchantments and their counts.
     */
    void onInventoryChanged(Map<Enchantment, Integer> newInventory);
}
