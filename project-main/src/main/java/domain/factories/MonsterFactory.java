/**
 * A factory class for creating monsters in the game.
 *
 * This class follows the Singleton pattern to ensure only one instance of the factory exists.
 * It provides functionality to create random monster types at specified positions.
 */
package domain.factories;

import domain.gameObjects.ArcherMonster;
import domain.gameObjects.FighterMonster;
import domain.gameObjects.Monster;
import domain.gameObjects.WizardMonster;
import technicalServices.logging.LogManager;

import java.util.Random;

public class MonsterFactory {
    private static MonsterFactory instance;
    /**
     * Retrieves the singleton instance of the MonsterFactory.
     *
     * If no instance exists, it creates a new one and logs the action.
     *
     * @return the singleton instance of the MonsterFactory
     */
    public static synchronized MonsterFactory getInstance() {
        if (instance == null) {
            instance = new MonsterFactory();
            LogManager.logInfo("MonsterFactory instance created. [from class: MonsterFactory, method: getInstance]");
        }
        return instance;
    }
    /**
     * Creates a random monster at the specified position.
     *
     * This method randomly selects one of three monster types (Archer, Fighter, or Wizard)
     * and creates an instance of the selected type at the given coordinates. Each creation
     * is logged for debugging and tracking purposes.
     *
     * @param x the x-coordinate of the monster's position
     * @param y the y-coordinate of the monster's position
     * @return the created Monster instance
     */
    public Monster createRandomMonster(int x, int y) {
        int type = new Random().nextInt(3);
        LogManager.logInfo("MonsterFactory created a random monster. [from class: MonsterFactory, method: createRandomMonster]");
        switch (type) {
            case 0:
                LogManager.logInfo("Archer Monster created INSIDE FACTORY before new--- -> " + x + ", " + y+"[from class: MonsterFactory, method: createRandomMonster]");
                System.out.println("Archer Monster created INSIDE FACTORY before new--- -> " + x + ", " + y);
                Monster archer = new ArcherMonster(x, y);
                LogManager.logInfo("Archer Monster created after new, position is -> " + archer.getPosition() + "[from class: MonsterFactory, method: createRandomMonster]");
                System.out.println("Archer Monster created after new, position is -> " + archer.getPosition());
                return archer;
            case 1:
                LogManager.logInfo("Fighter Monster created INSIDE FACTORY -> " + x + ", " + y+ "[from class: MonsterFactory, method: createRandomMonster]");
                System.out.println("Fighter Monster created INSIDE FACTORY -> " + x + ", " + y);
                Monster fighter = new FighterMonster(x, y);
                LogManager.logInfo("Fighter Monster created after new, position is -> " + fighter.getPosition()+ "[from class: MonsterFactory, method: createRandomMonster]");
                System.out.println("Fighter Monster created after new, position is -> " + fighter.getPosition());
                return fighter;
             case 2:
                 Monster wizard = new WizardMonster(x, y);
                 LogManager.logInfo("Wizard Monster created after new, position is -> " + wizard.getPosition() + "[from class: MonsterFactory, method: createRandomMonster]");
                 System.out.println("Wizard Monster created after new, position is -> " + wizard.getPosition());
                return wizard;
            default:
                return null;
        }
    }

}