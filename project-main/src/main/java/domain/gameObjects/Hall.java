/**
 * Represents a Hall within the game, which contains various game objects, monsters, and player interactions.
 */
package domain.gameObjects;

import domain.observers.HallObserver;
import domain.threads.MonsterThread;
import technicalServices.logging.LogManager;
import ui.gameObjectImage.RuneImage;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

import javax.swing.ImageIcon;
import domain.utilities.Constants;

public class Hall implements Serializable {
    private String name;
    private Map<Point, GameObject> gameObjects;
    private Player player;
    private int timeRemaining;
    private List<Monster> monsters = new ArrayList<>();
    private List<HallObserver> listeners = new ArrayList<>();
    private List<Point> emptyPositions = new ArrayList<>();
    private Map<Point, Rune> runeObjects;
    private Constants.HallType hallType;
    private int totalTime;

    public Hall(String name, Player player, int timeLimit, Constants.HallType hallType) {
        this.name = name;
        this.player = player;
        this.hallType = hallType;
        this.timeRemaining = timeLimit;
        this.gameObjects = new HashMap<>();
        this.emptyPositions = initializeEmptyPositions();
        this.runeObjects = new HashMap<>();
        this.totalTime = timeLimit;
    }

    private final List<Enchantment> activeEnchantments = new ArrayList<>();

    public void addEnchantment(Enchantment enchantment) {
        activeEnchantments.add(enchantment);
        System.out.println("Enchantment added: " + enchantment.getClass().getSimpleName());
        printInventory();
        // Trigger rendering or notify UI
    }

    public void printInventory() {
        // Get the inventory size
        int inventorySize = player.getEnchantments().size();

        // Print inventory size
        System.out.println("Inventory contains " + inventorySize + " item(s):");

        // Check if inventory is empty
        if (inventorySize == 0) {
            System.out.println("The inventory is empty.");
            return;
        }


    }


    public void removeEnchantment(Enchantment enchantment) {
        activeEnchantments.remove(enchantment);
        System.out.println("Enchantment removed: " + enchantment.getClass().getSimpleName());
        // Trigger UI update
    }

    public List<Enchantment> getActiveEnchantments() {
        return activeEnchantments;
    }

    public  int getTotalTime(){
        return totalTime;}

    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }
    public String getName() {
        return name;
    }

    public Constants.HallType getHallType() {
        return hallType;
    }

    public void setHallType(Constants.HallType hallType) {
        this.hallType = hallType;
    }

    public Map<Point, GameObject> getGameObjects() {
        return gameObjects;
    }

    public Player getPlayer() {
        return player;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public void decrementTimer() {
        timeRemaining--;
    }
    public Map<Point, Rune> getRuneObjects() {
        return runeObjects;
    }

    public void setRuneObjects(Map<Point, Rune> runeObjects) {
        this.runeObjects = runeObjects;
    }


    /** for tests made by: İpek Akbaytürk
     * Requires: The object cannot be null and the position of the object cannot be out of bounds. Hall cannot be full.
     * Modifies: gameObjects, emptyPositions
     * Effects: The object cannot be placed if the spot is occupied. Adds the object to the hall if the object is not null and the position of the object is not out of bounds.
                * When object is added, the emptyPositions array must be updated to reflect changes within the grid.
     * @param obj
     */
    public synchronized void addObject(GameObject obj) {

        if (obj == null){
            LogManager.logError("Object cannot be null. [from class: Hall, method: addObject]");
            throw new IllegalArgumentException("Object cannot be null.");
        }

        if (obj.getPosition().getX() > 15 || obj.getPosition().getY() > 15 || obj.getPosition().getX() < 0 || obj.getPosition().getY() < 0 || obj.getPosition() == null){
            LogManager.logError("Object position is out of bounds. [from class: Hall, method: addObject]");
            throw new IllegalArgumentException("Object position is out of bounds.");
        }

        // if hall is full, do not add object
        if (emptyPositions.isEmpty()){
            LogManager.logError("Hall is full. Cannot add object. [from class: Hall, method: addObject]");
            throw new IllegalArgumentException("Hall is full. Cannot add object.");
        }

        if (obj instanceof Rune){
            runeObjects.put(obj.getPosition(), (Rune) obj);

            //safeguard for rune behavior (must coincide with object for concealment)
            if (emptyPositions.contains(obj.getPosition())){
                emptyPositions.remove(obj.getPosition());
            }

            System.out.println("Rune added to the hall");
        }
        else if (gameObjects.containsKey(obj.getPosition())) {
            LogManager.logError("Position is already occupied by another object. Which object is it? " + gameObjects.get(obj.getPosition()).getClass().getSimpleName()+ " [from class: Hall, method: addObject]");
            throw new IllegalArgumentException("Position is already occupied by another object. Which object is it? " + gameObjects.get(obj.getPosition()).getClass().getSimpleName());
        }
        else {
            gameObjects.put(obj.getPosition(), obj);
            emptyPositions.remove(obj.getPosition());
            notifyListeners();
        }
    }
    public void notifyListeners() {
        for (HallObserver listener : listeners) {
            listener.onObjectAdded();
            listener.onObjectReplaced();
        }
    }
    public void addListener(HallObserver listener) {
        listeners.add(listener);
    }
    public void removeObject(Point position) {
        gameObjects.remove(position);
        emptyPositions.add(position);
    }
    public void removeMonster(Monster monster) {
        monsters.remove(monster);
    }
    public void movePlayer(Player p, Point newPosition) {
        // Remove the object from its current position and update it
        Point old = p.getPosition();
        player.setPosition(newPosition.x, newPosition.y);
        emptyPositions.add(old);
        emptyPositions.remove(newPosition);

    }
    public void moveObject(GameObject obj, Point newPosition) {
        if (!gameObjects.containsKey(obj.getPosition())) {
            LogManager.logError("Object does not exist in the hall. [from class: Hall, method: moveObject]");
            throw new IllegalArgumentException("Object does not exist in the hall.");
        }
        if (gameObjects.containsKey(newPosition)) {
            LogManager.logError("Target position is already occupied. [from class: Hall, method: moveObject]");
            throw new IllegalArgumentException("Target position is already occupied.");
        }

        Point old = obj.getPosition();
        gameObjects.remove(gameObjects.get(old));
        obj.setPosition(newPosition.x, newPosition.y);
        gameObjects.put(newPosition, obj);

        emptyPositions.add(old);
        emptyPositions.remove(newPosition);

    }
    /**
     * Retrieves the position of the active Luring Gem Enchantment.
     *
     * Iterates through the player's enchantments to locate an active Luring Gem Enchantment.
     * If an active Luring Gem is found, its position is returned. If no active Luring Gem is present, null is returned.
     *
     * @return The position of the active Luring Gem Enchantment, or null if no active gem exists.
     */
    public Point getLuringGemPosition(){
        int luringGemCount = 0;
        LuringGemEnchantment activeLuringGem = null;
        for (Enchantment enchantment : player.getEnchantments().keySet()) {
            if (enchantment instanceof LuringGemEnchantment) {
                if (((LuringGemEnchantment) enchantment).getIsActive() == 1){
                    activeLuringGem = (LuringGemEnchantment) enchantment;
                    return activeLuringGem.getPosition();

                }

                luringGemCount++;
            }
        }
        return null;
    }

    /** for tests made by: Uzay Ilgın
     * Requires:
     *   The hall's internal structures such as gameObjects, runeObject are properly initialized.
     * Modifies:
     *   The position of the Rune.
     *   No other objects are modified, and the Rune's old key is removed from `runeObjects` then inserted under its new key.
     * Effects:
     *   If a Rune is present in the hall, the method searches for at least one valid location to teleport the Rune.
     *   If at least one valid location is found different than current location, the Rune position is changed to a random valid location.
     *   If no valid locations exist, teleport to current location (it means keep it in same location).
     *   If no Rune is found at all, does nothing further.
     */

    /**
     * Teleports the Rune to a random valid location within the Hall.
     *
     * If a Rune exists, it is moved to a new valid position among objects such as Chest, Wall, Block, or WallDifferent.
     * If no valid locations are found, the Rune remains in its current position.
     */
    public void teleportRune() {
        Point runePos = null;
        Rune rune = null;

        for (Map.Entry<Point, Rune> entry : runeObjects.entrySet()) {
            runePos = entry.getKey();
            rune = (Rune) entry.getValue();
            break;
        }

        if (rune == null) {
            LogManager.logError("No Rune found to teleport. [from class: Hall, method: teleportRune]");
            System.out.println("No Rune found to teleport.");
            return;
        }
        // Remove rune from runeObjects
        runeObjects.remove(runePos);

        List<Point> validLocations = new ArrayList<>();
        for (Map.Entry<Point, GameObject> entry : gameObjects.entrySet()) {
            GameObject obj = entry.getValue();
            if (obj instanceof Chest || obj instanceof Wall || obj instanceof Block || obj instanceof WallDifferent) {
                validLocations.add(entry.getKey());
            }
        }

        if (validLocations.isEmpty()) {
            LogManager.logError("No valid locations found to teleport the Rune. [from class: Hall, method: teleportRune]");
            System.out.println("No valid locations found to teleport the Rune.");
            return;
        }

        Random random = new Random();
        Point newRunePos = validLocations.get(random.nextInt(validLocations.size()));
        // Set new position of the rune and put it in runeObjects map "Can be obtained from that map for further implementation"
        rune.setPosition(newRunePos.x, newRunePos.y);
        runeObjects.put(newRunePos, rune);
        int row = newRunePos.y;
        int col = newRunePos.x;
        LogManager.logInfo("Rune teleported to new position: " + row + "," + col + " [from class: Hall, method: teleportRune]");
        System.out.println("Rune teleported to new position: " + row + "," + col);
    }

    public synchronized Point getRandomEmptyPosition() {
        if (emptyPositions.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(emptyPositions.size());
        Point pos = emptyPositions.get(index);
        System.out.println("What is the empty position given to the factory? " + pos);
        return pos;
    }

    public List<Point> initializeEmptyPositions() {
        List<Point> emptyPositions = new ArrayList<>();
        for (int r = 0; r < 16; r++) {
            for (int c = 0; c < 16; c++) {
                Point pos = new Point(c, r);
                emptyPositions.add(pos);
            }
        }
        return emptyPositions;
    }
    public ArrayList<Point> getEmptyPositions() {
        return (ArrayList<Point>) emptyPositions;
    }

    public void addMonster(Monster monster) {
        monsters.add(monster);
    }

    public List<Monster> getMonsters() {
        return monsters;
    }
    public <T extends Monster> long countMonstersOfType(Class<T> monsterType) {
        return monsters.stream().filter(monsterType::isInstance).count();
    }

    public void setPlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        this.player = player;
    }

    public synchronized void moveObject(Point from, Point to) {
        GameObject object = gameObjects.remove(from);
        if (object != null) {
            gameObjects.put(to, object);
        }
        notifyListeners();
    }
    /**
     * Clears all objects and monsters from the hall.
     *
     * This method removes all game objects and monsters currently in the hall.
     * It also resets the list of empty positions to its initial state, making the hall ready for a fresh start.
     */
    public void clearObjects() {
        gameObjects.clear();

        monsters.clear();

        emptyPositions = initializeEmptyPositions();

        LogManager.logInfo("All game objects and monsters have been cleared. The hall is reset. [from class: Hall, method: clearObjects]");
        System.out.println("All game objects and monsters have been cleared. The hall is reset.");
    }
}