/**
 * The MonsterController class manages the creation and control of monsters in the game.
 *
 * This class interacts with the game state and the monster factory to spawn and
 * manage monsters. It ensures that monsters are spawned in valid positions
 * within the game hall.
 */
package domain.controllers;

import domain.factories.MonsterFactory;
import domain.gameCore.GameState;
import domain.gameObjects.ArcherMonster;
import domain.threads.MonsterThread;
import technicalServices.logging.LogManager;

import java.awt.*;

public class MonsterController {

    private GameState model;
    private MonsterFactory monsterFactory;

    public MonsterController(GameState model, MonsterFactory monsterFactory) {
        this.model = model;
        this.monsterFactory = monsterFactory;
        LogManager.logInfo("MonsterController initialized. [from class: MonsterController, method: MonsterController]");
        System.out.println("MonsterController initialized.");
    }
    /**
     * Spawns a random monster at a valid empty position in the game hall.
     *
     * This method retrieves a random empty position in the hall and spawns a monster
     * at that location using the game state's spawn logic. If the game state or hall is null,
     * the method logs an error and terminates the process.
     *
     * @param model the current game state containing the hall and spawning logic
     */
    public void spawnRandomMonster(GameState model) {
        if (model == null || model.getHall() == null) {
            LogManager.logError("Model or hall is null, cannot spawn monster. [from class: MonsterController, method: spawnRandomMonster]");
            System.err.println("Model or hall is null, cannot spawn monster.");
            return;
        }

        Point randPoint= model.getHall().getRandomEmptyPosition();
        model.spawnMonster(randPoint);
    }


}