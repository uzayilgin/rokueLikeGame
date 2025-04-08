/**
 * Manages the lifecycle of a game session, including initialization and transitions between game states.
 *
 * This class sets up the game environment, including the player, game state, window, and controller.
 * It also supports starting the game and transitioning to the build mode.
 */
package domain.gameCore;

import domain.behaviors.EarthHallStrategy;
import domain.behaviors.HallStrategy;
import domain.controllers.GameController;
import domain.gameObjects.Player;
import domain.threads.CustomTimer;
import domain.utilities.Constants;
import technicalServices.logging.LogManager;
import ui.swing.EarthHallScreen;
import ui.swing.GameWindow;

public class GameSession {

    Player player;
    GameState model;
    GameWindow window;
    GameController controller;
    HallStrategy hallStrategy;

    /**
     * Initializes a new GameSession with a default EarthHallStrategy.
     *
     * Sets up the player, game state, game window, and controller, and logs the initialization.
     */
    public GameSession() {
        LogManager.logInfo("GameSession initialized. [from class: GameSession, method: GameSession]");
        System.out.println("GameSession initialized");
        this.player = new Player(0, 0);
        LogManager.logInfo("Player initialized with position (0,0). [from class: GameSession, method: GameSession]");
        System.out.println("Player initialized with position (0,0)");
        this.model = new GameState(player, null);
        this.window = new GameWindow();
        this.controller = new GameController(model, window, player);
        this.hallStrategy = new EarthHallStrategy(player);
        model.setController(controller);
    }
    /**
     * Initializes a new GameSession with a specified HallStrategy.
     *
     * Sets up the player, game state, game window, and controller with the given strategy.
     *
     * @param hallStrategy the strategy for the hall in this game session
     */
    public GameSession(HallStrategy hallStrategy) {
        LogManager.logInfo("GameSession initialized. [from class: GameSession, method: GameSession]");
        System.out.println("GameSession initialized");
        this.player = new Player(0, 0);
        LogManager.logInfo("Player initialized with position (0,0). [from class: GameSession, method: GameSession]");
        System.out.println("Player initialized with position (0,0)");
        this.model = new GameState(player, null);
        model.setHallStrategy(hallStrategy);
        model.getHall().setHallType(hallStrategy.getHallType());
        this.window = new GameWindow();
        this.controller = new GameController(model, window, player);
        this.hallStrategy = hallStrategy;
        model.setController(controller);
    }

    /**
     * Starts the game by invoking the startGame method of the controller.
     */
    public void startGame() {

        controller.startGame();
    } /**
     * Starts the game and transitions to the building mode using the specified HallStrategy.
     */
    public void startAtBuilding() {
        controller.startGame();
        controller.switchToBuildMode(hallStrategy);

    }
}
