/**
 * A factory class for creating enchantments in the game.
 *
 * This class follows the Singleton pattern to ensure that only one instance
 * of the factory is created. It provides functionality to generate random enchantments
 * at specified positions.
 */
package domain.factories;

import domain.gameObjects.*;
import technicalServices.logging.LogManager;

import java.awt.*;
import java.util.Random;
import java.util.function.ToIntBiFunction;

public class EnchantmentFactory {
    private static EnchantmentFactory instance;
    /**
     * Retrieves the single instance of the EnchantmentFactory.
     *
     * If no instance exists, a new one is created and logged.
     *
     * @return the singleton instance of the EnchantmentFactory
     */
    public static synchronized EnchantmentFactory getInstance() {
        if (instance == null) {
            instance = new EnchantmentFactory();
            LogManager.logInfo("MonsterFactory instance created. [from class: MonsterFactory, method: getInstance]");
        }
        return instance;
    }
    /**
     * Spawns a random enchantment at the specified position.
     *
     * This method randomly selects an enchantment type and creates an instance
     * of the corresponding enchantment at the given coordinates.
     *
     * @param x the x-coordinate of the enchantment's position
     * @param y the y-coordinate of the enchantment's position
     * @return the created enchantment instance, or null if no type matches
     */
    public Enchantment spawnRandomEnchantment(int x, int y) {
        int type = new Random().nextInt(5);
        LogManager.logInfo("EnchantmentFactory created a random enchantment. [from class: EnchantmentFactory, method: spawnRandomEnchantment]");
        switch (type) {
            case 0:
                return new LifeEnchantment(x, y);
            case 1:
                return new CloakEnchantment(x, y);
            case 2:
                return new RevealEnchantment(x, y);
            case 3:
                return LuringGemEnchantment.getInstance(x, y);
            case 4:
                return new TimeEnchantment(x, y);
        }
        return null;
    }
}
