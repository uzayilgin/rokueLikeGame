/**
 * Represents a Data Transfer Object for the game's state.
 *
 * This class encapsulates information about the current state of the game,
 * including the player's position, life count, hall details, enchantments, and save metadata.
 */
package domain.dto;

import domain.gameObjects.Rune;
import domain.utilities.Constants;

import java.awt.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GameStateDto implements Serializable {
    private int playerPositionx;
    private int playerPositiony;
    private int playerLifeCount;
    private HashMap<Point, GameObjectDto> hallGameObjects;
    private int hallTimeRemaining;
    private Map<Point, Rune> hallRunes;
    private HashMap<Constants.GameObjectsInHall,Integer> playerEnchantments;
    private Date saveDate;
    private Constants.HallType hallType;

    public int getPlayerPositionx() {
        return playerPositionx;
    }

    public void setPlayerPositionx(int playerPositionx) {
        this.playerPositionx = playerPositionx;
    }

    public int getPlayerPositiony() {
        return playerPositiony;
    }

    public void setPlayerPositiony(int playerPositiony) {
        this.playerPositiony = playerPositiony;
    }

    public int getPlayerLifeCount() {
        return playerLifeCount;
    }

    public void setPlayerLifeCount(int playerLifeCount) {
        this.playerLifeCount = playerLifeCount;
    }

    public HashMap<Point, GameObjectDto> getHallGameObjects() {
        return hallGameObjects;
    }

    public void setHallGameObjects(HashMap<Point, GameObjectDto> hallGameObjects) {
        this.hallGameObjects = hallGameObjects;
    }

    public int getHallTimeRemaining() {
        return hallTimeRemaining;
    }

    public void setHallTimeRemaining(int hallTimeRemaining) {
        this.hallTimeRemaining = hallTimeRemaining;
    }

    public HashMap<Constants.GameObjectsInHall, Integer> getPlayerEnchantments() {
        return playerEnchantments;
    }

    public void setPlayerEnchantments(HashMap<Constants.GameObjectsInHall, Integer> playerEnchantments) {
        this.playerEnchantments = playerEnchantments;
    }
    public Map<Point, Rune> getHallRunes() {
        return hallRunes;
    }
    public void setHallRunes(Map<Point, Rune> hallRunes) {
        this.hallRunes = hallRunes;
    }
    public Constants.HallType getHallType() {
        return hallType;
    }
    public void setHallType(Constants.HallType hallType) {
        this.hallType = hallType;
    }
    public Date getSaveDate() {
        return saveDate;
    }
    public void setSaveDate(Date saveDate) {
        this.saveDate = saveDate;
    }
}
