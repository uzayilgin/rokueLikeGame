/**
 * The PlayerController class manages player interactions.
 *
 * This class handles player movement, interactions with game objects, the use of enchantments.
 * It interacts with the GameState to modify the player's state and the game environment as needed.
 */
package domain.controllers;

import domain.factories.MonsterFactory;
import domain.gameCore.GameState;
import domain.gameObjects.*;
import domain.threads.MonsterThread;
import domain.utilities.Constants;
import technicalServices.logging.LogManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerController {

    private GameState model;
    private boolean isPaused = false;
    /**
     * Constructs a PlayerController for the specified game state.
     *
     * Initializes the controller and logs its creation for debugging and monitoring purposes.
     *
     * @param model the current game state
     */
    public PlayerController(GameState model) {
        LogManager.logInfo("PlayerController initialized. [from class: PlayerController, method: PlayerController]");
        System.out.println("PlayerController initialized");
        this.model = model;
    }
    /**
     * Checks if the player currently has a Cloak enchantment.
     *
     * @return true if the player has at least one Cloak enchantment, false otherwise
     */
    public boolean hasCloak() {
        int CloakCount = 0;
        CloakEnchantment lastCloak = null;
        for (Enchantment enchantment : model.getPlayer().getEnchantments().keySet()) {
            if (enchantment instanceof CloakEnchantment) {
                lastCloak = (CloakEnchantment) enchantment;
                CloakCount++;
            }
        }


        System.out.println("Player has " + CloakCount + " cloaks");
        if (CloakCount > 0) {
            return true;
        }
        return false;
    }
    /**
     * Removes a Cloak enchantment from the player.
     *
     * If the player has one or more Cloak enchantments, this method removes the most recent one.
     *
     * @throws InstantiationException if an instantiation error occurs during enchantment removal
     * @throws IllegalAccessException if an illegal access error occurs during enchantment removal
     */
    public void removeCloak() throws InstantiationException, IllegalAccessException {
        int CloakCount = 0;
        CloakEnchantment lastCloak = null;
        for (Enchantment enchantment : model.getPlayer().getEnchantments().keySet()) {
            if (enchantment instanceof CloakEnchantment) {
                lastCloak = (CloakEnchantment) enchantment;
                CloakCount++;
            }
        }
        model.getPlayer().getEnchantments().remove(lastCloak);
        model.getPlayer().getEnchantments().remove(lastCloak);
    }
    /**
     * Removes a Reveal enchantment from the player.
     *
     * If the player has one or more Reveal enchantments, this method removes the most recent one.
     */
    public void removeReveal() {
        int revealCount = 0;
        RevealEnchantment lastReveal = null;

        for (Enchantment enchantment : model.getPlayer().getEnchantments().keySet()) {
            if (enchantment instanceof RevealEnchantment) {
                lastReveal = (RevealEnchantment) enchantment;
                revealCount++;
            }
        }
        model.getPlayer().getEnchantments().remove(lastReveal);
        model.getPlayer().getEnchantments().remove(lastReveal);
    }
    /**
     * Sets the attack damage for all ArcherMonster instances in the game.
     *
     * @param damage the new attack damage to set for ArcherMonsters
     */
    public void setArcherDamage(int damage) {
        for (MonsterThread monsterThread : model.getMonsterThreads()) {
            if (monsterThread.getMonster() instanceof ArcherMonster) {
                monsterThread.getMonster().setAttackDamage(damage);
                System.out.println(monsterThread.getMonster() + " attack damage: " + monsterThread.getMonster().getAttackDamage());
            }
        }
    }
    /**
     * Activates the Cloak enchantment, temporarily disabling damage from ArcherMonsters.
     *
     * The effect lasts for 6 seconds, after which ArcherMonsters' attack damage is restored.
     */
    public void useCloak() {
        setArcherDamage(0);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setArcherDamage(1);
                this.cancel();
            }
        }, 6000);


    }

    public boolean playerInteract(Point clickedPoint) {
        return model.interactWithObject(clickedPoint);
    }
    /**
     * Stops the player's movement.
     *
     * This method sets the player's movement flag to false and logs any errors encountered.
     */
    public void stopPlayerMoving() {
        try {
            model.getPlayer().setMoving(false);
        } catch (Exception e) {
            LogManager.logError("Error while stopping player movement. [from class: PlayerController, method: stopPlayerMoving]");
            System.err.println("Error while stopping player movement");
        }
    }


    public Point getPlayerCurrentPosition(){
        return model.getPlayer().getPosition();
    }
    /**
     * Moves the player one step to the left.
     */
    public void movePlayerLeft() {
        if (isPaused) return;

        Point currentPosition = getPlayerCurrentPosition();
        Point targetPosition = new Point(currentPosition.x - 1, currentPosition.y);

        synchronized (model.getHall()) {
            if (targetPosition.x >= 0 && !isCellOccupied(targetPosition)) {
                model.getPlayer().setDirection(Constants.Directions.LEFT);
                model.getPlayer().updatePosition();
                model.getHall().moveObject(currentPosition, targetPosition);
            }
        }
    }
    /**
     * Moves the player one step to the up.
     */
    public void movePlayerUp() {
        if (isPaused) return;
        synchronized (model.getHall()) {

            Point currentPosition = getPlayerCurrentPosition();
            Point targetPosition = new Point(currentPosition.x, currentPosition.y - 1);

            if (targetPosition.y >= 0 && !isCellOccupied(targetPosition)) {
                model.getPlayer().setDirection(Constants.Directions.UP);
                model.getPlayer().updatePosition();
                model.getHall().moveObject(currentPosition, targetPosition);
            }
        }
    }
    /**
     * Moves the player one step to the down.
     */
    public void movePlayerDown() {
        if (isPaused) return;

        Point currentPosition = getPlayerCurrentPosition();
        Point targetPosition = new Point(currentPosition.x, currentPosition.y + 1);

        synchronized (model.getHall()) {
            if (targetPosition.y < 16 && !isCellOccupied(targetPosition)) {
                model.getPlayer().setDirection(Constants.Directions.DOWN);
                model.getPlayer().updatePosition();
                model.getHall().moveObject(currentPosition, targetPosition);
            }
        }
    }
    /**
     * Moves the player one step to the right.
     */
    public void movePlayerRight() {
        if (isPaused) return;

        Point currentPosition = getPlayerCurrentPosition();
        Point targetPosition = new Point(currentPosition.x + 1, currentPosition.y);

        synchronized (model.getHall()) {
            if (targetPosition.x < 16 && !isCellOccupied(targetPosition)) {
                model.getPlayer().setDirection(Constants.Directions.RIGHT);
                model.getPlayer().updatePosition();
                model.getHall().moveObject(currentPosition, targetPosition);

            }
        }
    }
    /**
     * Checks if a specified cell in the game hall is occupied by a game object other than the player.
     *
     * This method retrieves the game objects in the hall and determines whether the specified
     * position is occupied. It excludes the player from the check, allowing the player to occupy
     * the same cell without conflict.
     *
     * @param targetPosition the position in the game hall to check
     * @return true if the cell is occupied by a game object other than the player,
     *         false otherwise
     */
    private boolean isCellOccupied(Point targetPosition) {
        Map<Point, GameObject> gameObjects = model.getHall().getGameObjects();
        return gameObjects.containsKey(targetPosition) && !(gameObjects.get(targetPosition) instanceof Player);
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}
