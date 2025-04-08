/**
 * The entry point for initializing and starting the game.
 *
 * This class is responsible for setting up the game session and starting the main game loop.
 * It also logs the initialization process for debugging and monitoring purposes.
 */
package domain.gameCore;

import technicalServices.logging.LogManager;

public class GameInitialization {
    public static void main(String[] args) {

        LogManager.logInfo("GameInitialization started. [from class: GameInitialization, method: main]");
        System.out.println("GameInitialization started.");
        GameSession game = new GameSession();
        game.startGame();

    }
}