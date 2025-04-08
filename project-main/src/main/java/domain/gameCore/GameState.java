/**
 * Represents the state of the game, including player, hall, monsters, and game settings.
 *
 * This class handles game state management, including saving, loading, and updating the state.
 * It also facilitates interactions between game entities and manages timers, hall strategies,
 * and gameplay transitions.
 */
package domain.gameCore;

import domain.behaviors.*;
import domain.controllers.GameController;
import domain.factories.MonsterFactory;
import domain.gameObjects.*;
import domain.serializers.GameStateSerializer;
import domain.threads.*;
import domain.utilities.Constants;
import technicalServices.logging.LogManager;
import technicalServices.persistence.adapters.FileAdapter;
import technicalServices.persistence.adapters.SaveGameAdapter;
import ui.gameObjectImage.RuneImage;
import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;
import javax.swing.ImageIcon;
import assets.audio.SoundProcessor;
import ui.swing.PlayMode;

public class GameState implements Serializable {

    private Player player = null;
    private Hall hall;
    private MonsterFactory monsterFactory;
    private boolean runningFlag;
    private GameController controller;
    private float remainingTime;
    private boolean isGameOver = false;
    private boolean isPlayMode;
    private final transient GameStateSerializer serializer = new GameStateSerializer();
    private transient SaveGameAdapter persistenceAdapter = new FileAdapter(serializer);
    private final List<MonsterThread> activeMonsterThreads = new ArrayList<>();
    private CustomTimer timer;
    private HallStrategy hallStrategy;
    private Date saveDate;
    // you need to create an instance of GameState object with player object, after
    // you can add another objects
    // into GameState.
    /**
     * Constructs a GameState with the specified player and controller.
     *
     * @param player the player object
     * @param controller the game controller
     */
    public GameState(Player player, GameController controller)  {
        if (player == null) {
            LogManager.logError("Player cannot be null. [from class: GameState, method: GameState]");
            throw new IllegalArgumentException("Player cannot be null");
        }
        this.controller = controller;
        this.player = player;
        this.runningFlag = true;
        // create singleton monster factory
        this.monsterFactory = MonsterFactory.getInstance();
        this.hall = new Hall("Default Hall", player, 30, getHallType());

    }
    /**
     * Constructs a GameState with the specified player, controller, and hall strategy.
     *
     * @param player the player object
     * @param controller the game controller
     * @param hallStrategy the strategy for the hall
     */
    public GameState(Player player, GameController controller, HallStrategy hallStrategy)  {
        if (player == null) {
            LogManager.logError("Player cannot be null. [from class: GameState, method: GameState]");
            throw new IllegalArgumentException("Player cannot be null");
        }
        this.hallStrategy = hallStrategy;
        this.controller = controller;
        this.player = player;
        this.runningFlag = true;
        // create singleton monster factory
        this.monsterFactory = MonsterFactory.getInstance();
        this.hall = new Hall("Default Hall", player, 30, hallStrategy.getHallType());

    }
    public Date getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(Date saveDate) {
        this.saveDate = saveDate;
    }
    public void setHallStrategy(HallStrategy hallStrategy) {
        this.hallStrategy = hallStrategy;
    }
    public void setPersistenceAdapter(SaveGameAdapter adapter) {
        this.persistenceAdapter = adapter;
    }
    public void setController(GameController controller) {
        this.controller = controller;
    }
    public CustomTimer getTimer() {return timer;}
    //will transfer the timer to gamestate dto
    public void setTimer(CustomTimer timer) {this.timer = timer;}
    public GameController getController() {
        return controller;
    }
    public Constants.HallType getHallType() {
        if (this.hallStrategy == null) {
            return Constants.HallType.EARTH;
        }
        return hallStrategy.getHallType();
    }
    // every time u need to call relatively object in your classes, you need to call
    // it with
    // GameState.getPlayer()
    public Player getPlayer() {
        return this.player;
    }

    public Hall getHall() {
        return this.hall;
    }
    public boolean repOk() {
        return player != null &&
                hall != null &&
                monsterFactory != null &&
                controller != null &&
                activeMonsterThreads != null;
    }

    // this method is used to check if the position is empty
    public boolean isPositionEmpty(Point position) {
        if (this.hall.getEmptyPositions().contains(position)) {
            return true;
        } else {
            return false;
        }
    }


    public void update() {
        player.update();
    }

    public float getRemainingTime() {
        return this.remainingTime;
    }

    // usage: if user pauses the game, everything should be stopped.
    public boolean freeze() {
        // freeze every object
        // if successfully stopped everything
        // add logging here (State is frozen)
        // this.timer.stop();
        this.runningFlag = false;
        LogManager.logInfo("State is frozen. [from class: GameState, method: freeze]");
        return true;
    }


    public boolean isPaused() {
        return !this.runningFlag;
    }

    public boolean unfreeze() {
        this.runningFlag = true;
        LogManager.logInfo("State is unfrozen. [from class: GameState, method: unfreeze]");
        return true;
    }
    /**
     * Checks if a given point is adjacent to the player's current position.
     *
     * @param clickedPoint the point to check for adjacency
     * @return true if the point is adjacent to the player, false otherwise
     */
    private boolean isAdjacentToPlayer(Point clickedPoint) {
        // Get player position
        Point playerPosition = player.getPosition();

        // Precompute adjacent points
        Set<Point> adjacentPoints = new HashSet<>();
        adjacentPoints.add(new Point(playerPosition.x + 1, playerPosition.y)); // Right
        adjacentPoints.add(new Point(playerPosition.x - 1, playerPosition.y)); // Left
        adjacentPoints.add(new Point(playerPosition.x, playerPosition.y + 1)); // Below
        adjacentPoints.add(new Point(playerPosition.x, playerPosition.y - 1)); // Above

        // Check if clicked point is in the adjacent points set
        return adjacentPoints.contains(clickedPoint);
    }
    /**
     * Handles interactions between the player and an object at the specified point.
     *
     * If the clicked point is adjacent to the player and contains an interactable object,
     * the interaction is processed based on the object's type.
     *
     * @param clickedPoint the point where the interaction is attempted
     * @return true if an interaction occurred, false otherwise
     */
    public boolean interactWithObject(Point clickedPoint) {
        // if clicked point is not adjacent to player, return false
        if (!isAdjacentToPlayer(clickedPoint)) {
            return false;
        }

        Point playerPosition = player.getPosition();

        // check for adjacent objects
        if (hall.getGameObjects().containsKey(clickedPoint)) {
            GameObject object = hall.getGameObjects().get(clickedPoint);

            //check for interactables
            if (object instanceof Chest || object instanceof Wall || object instanceof WallDifferent
                    || object instanceof Block) {
                Map<Point, Rune> runeMap = hall.getRuneObjects();

                // if rune exists at clicked point, reveal it and replace object img with rune img
                if (runeMap.containsKey(clickedPoint)) {
                    System.out.println("Type of Rune found is: " + runeMap.get(clickedPoint).getRuneType());
                    successfulHallCompletion(object, clickedPoint, runeMap);
                }
                return true;
            }

            if(object instanceof LifeEnchantment) {
                Enchantment enchantment = (Enchantment) object;
                if (enchantment instanceof LifeEnchantment) {
                    if (player.getLifeCount() == 1 || player.getLifeCount() == 2 || player.getLifeCount() == 3) {
                        int oldHealth = player.getLifeCount();
                        if (oldHealth != 3) {
                            player.setLifeCount(player.getLifeCount() + 1);
                            hall.removeEnchantment(enchantment);

                        }
                        System.out.println("eskican= " + oldHealth + " şimdikican= " + player.getLifeCount());
                        if (controller.getHealthHeartDisplay() != null) {
                            controller.getHealthHeartDisplay().onHealthChanged(player.getLifeCount(), oldHealth);
                        }
                    }
                }
                hall.removeObject(((Enchantment) object).getPosition());
                hall.getGameObjects().remove(clickedPoint);
                hall.notifyListeners();
                return true;
            }
            else if (object instanceof CloakEnchantment || object instanceof LuringGemEnchantment || object instanceof RevealEnchantment){

                Enchantment enchantment = (Enchantment) object;
                //commenting this out bc addenchantment and collectenchantment are the same methods
                //since the implementation for the enchantments are done by Zeliha and Gül
                //i am leaving this here bc it might cause other bugs
                //but collectenchantment is an unnecessary method that is same with addenchantment
                //player.addEnchantment(enchantment);
                player.collectEnchantment(enchantment);
                hall.removeObject(((Enchantment) object).getPosition());
                hall.getGameObjects().remove(clickedPoint);
                hall.notifyListeners();
                if (controller.getView() instanceof PlayMode) {
                    PlayMode playMode = (PlayMode) controller.getView();
                    playMode.getInventoryPanel().updateInventory();
                }
                return true;
            }

            else if(object instanceof TimeEnchantment){
                Enchantment enchantment = (Enchantment) object;
                hall.removeObject(((Enchantment) object).getPosition());
                hall.getGameObjects().remove(clickedPoint);
                remainingTime = this.getTimer().getTimeRemaining();
                System.out.println("Time before: " + remainingTime);
                remainingTime += 5.0f;
                System.out.println("Time after: " + remainingTime);
                controller.getTimerDisplay().setTime((int)remainingTime);
                hall.notifyListeners();
                return true;
            }
        }

        return false;
    }
    /**
     * Completes the current hall when the player successfully interacts with a rune.
     *
     * This method reveals the rune, plays a sound effect, and transitions to the next hall or ends the game.
     *
     * @param object the object at the clicked point
     * @param clickedPoint the point where the interaction occurred
     * @param runeMap the map of runes in the hall
     */
public void successfulHallCompletion(GameObject object, Point clickedPoint, Map<Point, Rune> runeMap) {
        Rune rune = runeMap.get(clickedPoint);
        rune.setRevealed(true);
        rune.reveal();
        RuneImage runeImage = new RuneImage();
        ImageIcon runeIcon = runeImage.getImageForEntity(rune);
        object.setIcon(runeIcon);
        controller.updatePlayModeGrid(clickedPoint, runeIcon);
        // sound is played to signify end of the current level
        SoundProcessor doorSound = new SoundProcessor("src/main/java/assets/audio/doorOpening.wav");
        doorSound.playSound();

        // wait for 2 seconds (simulate delay before stopping the level)
//        try {
//            Thread.sleep(2000); // 1 second delay
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            LogManager.logError("Error while waiting for 1 second. [from class: GameState, method: successfulHallCompletion]");
//        }


        LogManager.logInfo("Level has been stopped. [from class: GameState, method: stopLevel]");
        if (hall.getHallType() == Constants.HallType.EARTH) {
            timer.stop();
            controller.switchToAirHallScreen();
        } else if (hall.getHallType() == Constants.HallType.AIR) {
            timer.stop();
            controller.switchToWaterHallScreen();
        } else if (hall.getHallType() == Constants.HallType.WATER) {
            timer.stop();
            controller.switchToFireHallScreen();
        } else {
            timer.stop();
            controller.successfulEnding();
            this.isGameOver = true;
            runningFlag = false;
        }
    }
    public int getTotalTime() {
        int totalTime = 0;
        if (hall.getHallType() == Constants.HallType.EARTH) {
            totalTime = 30;
        }else if (hall.getHallType() == Constants.HallType.FIRE) {
            totalTime = 85;
        }else if (hall.getHallType() == Constants.HallType.WATER) {
            totalTime = 65;
        }else if (hall.getHallType() == Constants.HallType.AIR) {
            totalTime = 45;
        }
        return totalTime;
    }

    public boolean isGameOver() {
        return this.isGameOver;
    }

    public void gameOver() {
        this.isGameOver = true;
    }

    public void resetGame() {
        hall.clearObjects();
        hall.setPlayer(player);
        player.setPosition(new Point(0, 0));
        player.setLifeCount(3);
        this.runningFlag = true;
        LogManager.logInfo("Game has been reset. [from class: GameState, method: resetGame]");
        System.out.println("Game has been reset.");
        // this.timer.kill();
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }


    public List<MonsterThread> getMonsterThreads(){
        return this.activeMonsterThreads;
    }

    public boolean hasActiveMonsterThreads() {
        return !activeMonsterThreads.isEmpty();
    }

    //this method is duplicated to only spawn fighter monsters but after enchantment tests it can be removed
    //bc it is same with the spawnMonster method
    public void spawnFighterMonster(Point position) {
//        Monster monster = monsterFactory.createRandomMonster(position.x, position.y);
        Monster monster = new FighterMonster(position.x, position.y);
        // System.out.println("Which monster is being spawned to what position? " +
        // monster.getPosition() + " " + monster.getClass().getSimpleName());
        if (monster instanceof WizardMonster && hall.countMonstersOfType(WizardMonster.class) >= 1) {
            System.out.println("Cannot spawn more than 1 WizardMonster.");
            LogManager.logInfo("Cannot spawn more than 1 WizardMonster. [from class: GameState, method: spawnMonster]");
            return;
        }
        hall.addMonster(monster);
        hall.addObject(monster);
        hall.getGameObjects().put(position, monster);
        System.out.println("Which monster is being spawned to what position? " + monster.getPosition() + " "
                + monster.getClass().getSimpleName());
        LogManager.logInfo("Monster " + monster.getClass().getSimpleName() + " has been spawned at position " + position
                + ". [from class: GameState, method: spawnMonster]");
        // here create a thread for the monster created
        MonsterThread monsterThread = switch (monster) {
            case ArcherMonster archerMonster1 -> new ArcherMonsterThread(archerMonster1, this);
            case FighterMonster fighterMonster1 -> new FighterMonsterThread(fighterMonster1, this);
            case WizardMonster wizardMonster1 -> new WizardMonsterThread(wizardMonster1, this);
            default -> throw new IllegalArgumentException("Unknown monster type");
        };
        activeMonsterThreads.add(monsterThread);
        Thread thread = new Thread(monsterThread);
        thread.start();
    }
    public void setMonsterFactory(MonsterFactory monsterFactory) {
        this.monsterFactory = monsterFactory;
    }
    // generic for adding any monster to hall but their behaviours are different
    // the factory will create the monsters
    /**
     * Spawns a random monster at the specified position.
     *
     * This method uses the monster factory to create a random monster. If the monster is a WizardMonster,
     * it ensures that only one WizardMonster exists in the hall at a time. The created monster is added to
     * the hall and the game objects map. A corresponding thread is started to manage the monster's behavior.
     *
     * @param position the position where the monster will be spawned
     */
    public void spawnMonster(Point position) {
        Monster monster = monsterFactory.createRandomMonster(position.x, position.y);
        if (monster instanceof WizardMonster && hall.countMonstersOfType(WizardMonster.class) >= 1) {
            System.out.println("Cannot spawn more than 1 WizardMonster.");
            LogManager.logInfo("Cannot spawn more than 1 WizardMonster. [from class: GameState, method: spawnMonster]");
            return;
        }
        hall.addMonster(monster);
        hall.addObject(monster);
        hall.getGameObjects().put(position, monster);
        System.out.println("Which monster is being spawned to what position? " + monster.getPosition() + " "
                + monster.getClass().getSimpleName());
        LogManager.logInfo("Monster " + monster.getClass().getSimpleName() + " has been spawned at position " + position
                + ". [from class: GameState, method: spawnMonster]");
        // here create a thread for the monster created
        MonsterThread monsterThread = switch (monster) {
            case ArcherMonster archerMonster1 -> new ArcherMonsterThread(archerMonster1, this);
            case FighterMonster fighterMonster1 -> new FighterMonsterThread(fighterMonster1, this);
            case WizardMonster wizardMonster1 -> new WizardMonsterThread(wizardMonster1, this);
            default -> throw new IllegalArgumentException("Unknown monster type");
        };
        activeMonsterThreads.add(monsterThread);
        Thread thread = new Thread(monsterThread);
        thread.start();
    }

    public void setPlayMode(boolean isPlayMode) {
        this.isPlayMode = isPlayMode;
    }

    public boolean isPlayMode() {
        return this.isPlayMode;
    }
    public void saveGame(String gameName) {
        if (persistenceAdapter != null) {
            persistenceAdapter.saveGame(gameName, this);
        }
    }

    /**
     * Loads a saved game state using the specified game name.
     *
     * This method retrieves the saved game state from the persistence adapter and updates the current game state
     * with the loaded data, including player information, hall state, and game status.
     *
     * @param gameName the name of the saved game to load
     */
    //this is a crucial method for loading the game
    public void loadGame(String gameName) {
        if (persistenceAdapter != null) {
            GameState loadedState = persistenceAdapter.loadGame(gameName);
            this.player = loadedState.player;
            this.hall = loadedState.hall;
            this.runningFlag = loadedState.runningFlag;
            this.isGameOver = loadedState.isGameOver;
            this.isPlayMode = loadedState.isPlayMode;
            this.controller = loadedState.controller;
            this.saveDate = loadedState.saveDate;
        }
    }
    public ArrayList<String> loadSavedGames() {
        if (persistenceAdapter != null) {
            return new ArrayList<>(persistenceAdapter.listSavedGames());
        }
        return new ArrayList<>();
    }
    public void deleteSavedGame(String gameName) {
        persistenceAdapter.deleteSavedGame(gameName);
    }
}