/**
 * The EnchantmentController class manages the creation, placement, and handling
 * of enchantments in the game.
 *
 * It interacts with the game state, hall, and enchantment factory to spawn new
 * enchantments at random positions and initiate their behavior using threads.
 *
 */
package domain.controllers;

import domain.factories.EnchantmentFactory;
import domain.gameCore.GameState;
import domain.gameObjects.*;
import domain.threads.EnchantmentThread;
import technicalServices.logging.LogManager;

import java.awt.Point;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class EnchantmentController {

    private Hall hall; // Hall where enchantments are stored and managed
    private EnchantmentFactory enchantmentFactory; // Factory to create enchantments
    private GameState model;
    /**
     * Constructs an EnchantmentController with the specified game state and enchantment factory.
     *
     * @param model the current game state
     * @param enchantmentFactory the factory used to create enchantments
     */
    public EnchantmentController(GameState model, EnchantmentFactory enchantmentFactory) {
        this.hall = model.getHall();
        this.enchantmentFactory = enchantmentFactory;
        this.model = model;

    }
    /**
     * Spawns a new enchantment at a random empty position in the hall.
     *
     * The enchantment is added to the hall and managed as a game object. A thread is started to handle the behavior
     * of the spawned enchantment.
     */
    public void spawnEnchantment() {
        Point position = hall.getRandomEmptyPosition();

        Enchantment enchantment = enchantmentFactory.spawnRandomEnchantment(position.x, position.y);

        hall.addEnchantment(enchantment);
        hall.addObject(enchantment);
        hall.getGameObjects().put(position, enchantment);
        System.out.println("Enchantment spawned at " + enchantment.getPosition() + " of type " + enchantment.getClass().getSimpleName());
        LogManager.logInfo("Enchantment " + enchantment.getClass().getSimpleName() + " has been spawned at position " + position);
        EnchantmentThread enchantmentThread = new EnchantmentThread(enchantment, model);
        Thread thread = new Thread(enchantmentThread);
        thread.start();
    }




}
