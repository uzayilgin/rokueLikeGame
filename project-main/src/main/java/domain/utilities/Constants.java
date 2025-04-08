/**
 * A utility class for defining constants used throughout the game.
 * This class includes inner classes, enums, and static methods for player directions,
 * game object types, and other constants.
 */
package domain.utilities;

public class Constants {
    public static class Directions{
        public static final int LEFT = 0;
        public static final int RIGHT = 2;
        public static final int UP = 1;
        public static final int DOWN = 3;
    }
    public static class PlayerConstants{
        public static final int IDLE_FRONT = 0;


        public static int GetSpriteAmount(int player_action) {
            switch (player_action) {
                case IDLE_FRONT:
                    return 1;
                default:
                    return 1;
            }
        }
    }

    public enum HallType { 
        EARTH, 
        FIRE, 
        WATER, 
        AIR }

    public enum MonsterType {
        ARCHER,
        FIGHTER,
        WIZARD
    }
    //This enum is used to determine the type of the game object
    // for deserialize the game object
    //i will pass dto the type of the object as enum and the position of the object
    public enum GameObjectsInHall{
        WIZARD,
        ARCHER,
        FIGHTER,
        CHEST,
        BLOCK,
        WALL,
        WALLDIFFERENT,
        LIFEENCHANTMENT,
        REVEALENCHANTMENT,
        LURINGENCHANTMENT,
        CLOAKENCHANTMENT,
        TIMEENCHANTMENT,
        EMPTY

    }
}
