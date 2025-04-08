/**
 * The Monster class is an abstract base class for all types of monsters in the game.
 */
package domain.gameObjects;

import domain.utilities.Constants;
import technicalServices.logging.LogManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public abstract class Monster extends GameObject {
    public static final Logger logger = Logger.getLogger(Monster.class.getName());
    private int x;
    private int y;
    private int attackDamage = 1;
    public Monster(int x, int y) {
        super(x, y);
    }
    public Point getPosition() {
        return super.getPosition();
    }
    //this is probably going to be removed, spawning is done in another layer by factory
   /* public void spawn (Point newPosition){
        if (newPosition==null){
            logger.warning("Given position is null, can not spawn monster");
            return;
        }
        position.setLocation(newPosition);
    };*/
    //this will be overridden by the subclasses, each monster have different attack method
    public abstract void attack(Player player);
    // this will be only overridden by archer and fighter
    //archer moves in 1 second
    //fighter moves in random seconds but can be fooled by luring gem enchantment
    public abstract void move(ArrayList<Point> emptyLocations);
    //this also will be overridden by nothing :)) maybe we can delete this
    public abstract void interact();
    /**
     * Finds a random location from the provided list of possible locations.
     *
     * @param possibleLocations the list of possible locations to choose from
     * @return a randomly selected Point from the list, or null if the list is null or empty
     */
    public static Point findRandomLocation(ArrayList<Point> possibleLocations){
        if (possibleLocations==null|| possibleLocations.isEmpty() ){
            LogManager.logInfo("Given list of possible locations is null or empty, can not find random location. [from class: Monster, method: findRandomLocation]");
            System.err.println("Given list of possible locations is null or empty, can not find random location");
            return null;
        }
        int randomIndex = (int) (Math.random() * possibleLocations.size());
        return possibleLocations.get(randomIndex);
    };
    /**
     * Calculates the Euclidean distance between two points.
     *
     * @param p1 the first point
     * @param p2 the second point
     * @return the distance between the two points, or -1 if either point is null
     */
    public static int calculateDistance(Point p1, Point p2){
        if (p1==null || p2==null){
            LogManager.logError("Given points are null, can not calculate distance. [from class: Monster, method: calculateDistance]");
            System.err.println("Given points are null, can not calculate distance.");
            return -1;
        }
        return (int) Math.sqrt(Math.pow(p1.getX()-p2.getX(),2)+Math.pow(p1.getY()-p2.getY(),2));
    };

    public abstract void render(Graphics g);

    public int getAttackDamage() {
        return attackDamage;
    }
    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }
}