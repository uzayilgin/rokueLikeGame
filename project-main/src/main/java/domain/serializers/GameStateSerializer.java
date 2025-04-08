/**
 * Handles the serialization and deserialization of the game state.
 * Converts `GameState` objects to `GameStateDto` for persistent storage and vice versa.
 * Ensures all player-related, hall-related, and enchantment-related data is properly managed during the process.
 */
package domain.serializers;

import domain.controllers.GameController;
import domain.dto.CollectedEnchantmentDto;
import domain.dto.GameObjectDto;
import domain.dto.GameStateDto;
import domain.gameCore.GameState;
import domain.gameObjects.*;
import domain.threads.*;
import domain.utilities.Constants;

import java.awt.*;
import java.util.Date;
import java.util.HashMap;

import static domain.utilities.Constants.GameObjectsInHall.*;

public class GameStateSerializer {
    /**
     * Serializes the current state of the game into a `GameStateDto` object.
     *
     * @param gameState The current state of the game to be serialized.
     * @return A `GameStateDto` object containing all relevant game data.
     */
    public GameStateDto serializeGameState(GameState gameState) {
        GameStateDto gameStateDto = new GameStateDto();
        //all player related data is transferred to the dto
        gameStateDto.setPlayerPositionx(gameState.getPlayer().getPosition().x);
        gameStateDto.setPlayerPositiony(gameState.getPlayer().getPosition().y);
        gameStateDto.setPlayerLifeCount(gameState.getPlayer().getLifeCount());
        HashMap<Constants.GameObjectsInHall, Integer> playerEnchantments = new HashMap<>();
        //here type of the enc comes as null
        gameState.getPlayer().getEnchantments().forEach((enchantment, integer) -> {
            CollectedEnchantmentDto collectedEnchantmentDto = new CollectedEnchantmentDto();
            collectedEnchantmentDto.setType(enchantment.getType());
            //putting the enchantment dto directly to the hashmap causes the issue of not being able to deserialize it
            playerEnchantments.put(collectedEnchantmentDto.getType(), integer);
        });
        gameStateDto.setPlayerEnchantments(playerEnchantments);
        // all hall related data is transferred to the dto, hall type added
        HashMap<Point, GameObjectDto> hallGameObjects = new HashMap<>();
        gameState.getHall().getGameObjects().forEach((point, gameObject) -> {
            GameObjectDto gameObjectDto = new GameObjectDto();
            gameObjectDto.setX(gameObject.getPosition().x);
            gameObjectDto.setY(gameObject.getPosition().y);
            gameObjectDto.setType(gameObject.getType());
            hallGameObjects.put(point, gameObjectDto);
        });
        gameStateDto.setHallGameObjects(hallGameObjects);
        gameStateDto.setHallTimeRemaining(gameState.getTimer().getTimeRemaining());
        gameStateDto.setHallRunes(gameState.getHall().getRuneObjects());
        gameStateDto.setHallType(gameState.getHall().getHallType());
        gameStateDto.setSaveDate(new Date());
        return gameStateDto;
    }
    /**
     * Deserializes a `GameStateDto` object into a `GameState` object.
     *
     * @param dto The serialized game state data.
     * @param gameController The game controller instance to bind the deserialized state.
     * @return A `GameState` object reconstructed from the `GameStateDto`.
     */
    public GameState deserializeGameState(GameStateDto dto, GameController gameController) {
        GameState gameState = new GameState(new Player(dto.getPlayerPositionx(), dto.getPlayerPositiony()), gameController);
        //set up player related data: position, life count, enchantments
        gameState.getPlayer().setLifeCount(dto.getPlayerLifeCount());
        HashMap<Constants.GameObjectsInHall, Integer> playerEnchantments = dto.getPlayerEnchantments();
        playerEnchantments.forEach((collectedEnchantmentType, integer) -> {
            for (int i = 0; i < integer; i++) {
                switch (collectedEnchantmentType) {
                    case CLOAKENCHANTMENT -> gameState.getPlayer().addEnchantment(new CloakEnchantment());
                    case LURINGENCHANTMENT -> gameState.getPlayer().addEnchantment(new LuringGemEnchantment());
                    case REVEALENCHANTMENT -> gameState.getPlayer().addEnchantment(new RevealEnchantment());
                    case LIFEENCHANTMENT -> gameState.getPlayer().addEnchantment(new LifeEnchantment());
                }
            }
        });
        //set up hall related data: all hall objects, time remaining, rune
        gameState.getHall().setTimeRemaining(dto.getHallTimeRemaining());
        //instead of this maybe we can set the halls timer to the remaining time but i am not sure how is the current impl is
        //gameState.setTimer(new CustomTimer(dto.getHallTimeRemaining()));
        dto.getHallGameObjects().forEach((point, gameObjectDto) -> {
            GameObject gameObject = switch (gameObjectDto.getType()) {
                case CLOAKENCHANTMENT -> new CloakEnchantment(gameObjectDto.getX(), gameObjectDto.getY());
                case LURINGENCHANTMENT -> new LuringGemEnchantment(gameObjectDto.getX(), gameObjectDto.getY());
                case REVEALENCHANTMENT -> new RevealEnchantment(gameObjectDto.getX(), gameObjectDto.getY());
                case LIFEENCHANTMENT -> new LifeEnchantment(gameObjectDto.getX(), gameObjectDto.getY());
                case TIMEENCHANTMENT -> new TimeEnchantment(gameObjectDto.getX(), gameObjectDto.getY());
                case WIZARD -> new WizardMonster(gameObjectDto.getX(), gameObjectDto.getY());
                case ARCHER -> new ArcherMonster(gameObjectDto.getX(), gameObjectDto.getY());
                case FIGHTER -> new FighterMonster(gameObjectDto.getX(), gameObjectDto.getY());
                case WALL -> new Wall(gameObjectDto.getX(), gameObjectDto.getY());
                case CHEST -> new Chest(gameObjectDto.getX(), gameObjectDto.getY());
                case WALLDIFFERENT -> new WallDifferent(gameObjectDto.getX(), gameObjectDto.getY());
                case BLOCK -> new Block(gameObjectDto.getX(), gameObjectDto.getY());
                default -> null;
            };

            if (gameObject != null) {
                gameState.getHall().addObject(gameObject);
                if (gameObject instanceof Monster) {
                    MonsterThread monsterThread = switch ((Monster) gameObject) {
                        case ArcherMonster archerMonster -> new ArcherMonsterThread(archerMonster, gameState);
                        case FighterMonster fighterMonster -> new FighterMonsterThread(fighterMonster, gameState);
                        case WizardMonster wizardMonster -> new WizardMonsterThread(wizardMonster, gameState);
                        default -> throw new IllegalArgumentException("Unknown monster type");
                    };
                    gameState.getMonsterThreads().add(monsterThread);
                    new Thread(monsterThread).start();
                }

                if (gameObject instanceof Enchantment) {
                    EnchantmentThread enchantmentThread = new EnchantmentThread((Enchantment) gameObject, gameState);
                    new Thread(enchantmentThread).start();
                }
            }
        });
        gameState.getHall().setRuneObjects(dto.getHallRunes());
        //setting hall type
        gameState.getHall().setHallType(dto.getHallType());
        //this is done to fix the problem of empty constructor for the game controller
        // the game controller is set after the game state is created
        gameController.setGameState(gameState);
        gameController.setPlayer(gameState.getPlayer());
        Date saveDate = dto.getSaveDate();
        gameState.setSaveDate(saveDate);
        return gameState;

    }
}
