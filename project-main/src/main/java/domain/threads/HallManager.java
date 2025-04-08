/**
 * HallManager is responsible for managing the spawning of monsters and enchantments in the game hall.
 * It operates as a separate thread to ensure continuous gameplay updates, even while other game logic executes.
 * The manager handles synchronization, pausing, and resuming, and can be terminated when the game ends.
 */

package domain.threads;

import domain.controllers.EnchantmentController;
import domain.controllers.MonsterController;
import domain.gameCore.GameState;
import domain.gameObjects.Enchantment;
import technicalServices.logging.LogManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class HallManager implements Runnable {

    private GameState model;
    private MonsterController controller;
    private Random random = new Random();
    private boolean isAlive = true;
    private static final List<HallManager> activeManagers = new ArrayList<>();
    private EnchantmentController enchantmentController;


    public HallManager(GameState model, MonsterController controller, EnchantmentController enchantmentController) {
        this.model = model;
        this.controller = controller;
        synchronized (activeManagers) {
            activeManagers.add(this);
        }
        this.enchantmentController = enchantmentController;
    }

    public void kill() {
        isAlive = false;
    }
    /**
     * Executes the main logic of the HallManager thread.
     * Periodically spawns monsters and enchantments, while respecting the game's paused state and game over condition.
     */

    @Override
    public void run() {

        while (isAlive) {


            while (model.isPaused()) {
                //do nothing
                LogManager.logInfo("Hall manager paused. [from class: HallManager, method: run]");
                System.out.println("Hall manager paused");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            LogManager.logInfo("Hall manager resumed. [from class: HallManager, method: run]");
            System.out.println("Hall manager resumed");
            controller.spawnRandomMonster(model);

            try {
                controller.spawnRandomMonster(model);
                Thread.sleep(12000);
                while (model.isPaused()) {
                    //do nothing
                    LogManager.logInfo("Hall manager paused. [from class: HallManager, method: run]");
                    System.out.println("Hall manager paused");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                enchantmentController.spawnEnchantment();
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (model.isGameOver()) {
                break;
            }
        }

        LogManager.logInfo("Hall manager thread terminating. [from class: HallManager, method: run]");
        synchronized (activeManagers) {
            activeManagers.remove(this);
        }
    }
    /**
     * Stops all active HallManager threads.
     * Ensures synchronized termination of all HallManager instances running in the game.
     */

    public static void stopAllThreads() {
        if (activeManagers.isEmpty()) {
            return;
        }

        synchronized (activeManagers) {
            for (HallManager manager : activeManagers) {
                manager.kill();
            }
            activeManagers.clear();
        }
    }

    /*public void stopAllMonsterThreads() {
        List<MonsterThread> activeMonsterThreads = model.getMonsterThreads();
        for (MonsterThread thread : activeMonsterThreads) {
            thread.kill();
        }
        activeMonsterThreads.clear();
    }*/

}
