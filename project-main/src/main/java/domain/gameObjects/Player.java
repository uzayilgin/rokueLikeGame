/**
 * Represents the player character in the game, including attributes such as position, life count, and collected enchantments.
 * Provides methods for movement, interaction, rendering, and managing inventory of enchantments.
 */
package domain.gameObjects;

import domain.observers.HealthObserver;
import domain.observers.InventoryObserver;

import static domain.utilities.Constants.Directions.DOWN;
import static domain.utilities.Constants.Directions.LEFT;
import static domain.utilities.Constants.Directions.RIGHT;
import static domain.utilities.Constants.Directions.UP;
import java.awt.*;
import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player extends GameObject implements Serializable {
    private Point position;
    private int lifeCount;
    private ArrayList<HealthObserver> observers;
    private Image playerImage;
    private int playerDir;
    private boolean moving;
    private static final int PLAYER_SIZE = 32;
    private static final int SPEED = 1;
    private HashMap<Enchantment,Integer> enchantments = new HashMap<>();

    public Player(int x, int y) {
        super(x, y);
        this.position = new Point(x, y);
        this.lifeCount = 3;
        this.observers = new ArrayList<>();
        this.playerDir = -1;
        this.moving = false;
        loadPlayerImage();
        notifyObservers();
    }

    /**
     * Adds a specified enchantment to the player's inventory.
     * If the enchantment already exists, its count is incremented; otherwise, it is added with an initial count of 1.
     *
     * @param enchantment the enchantment to be collected
     */
    public void collectEnchantment(Enchantment enchantment) {
        // Check if the enchantment already exists in the inventory
        if (enchantments.containsKey(enchantment)) {
            // If it's already in the inventory, increase the count
            enchantments.put(enchantment, enchantments.get(enchantment) + 1);
        } else {
            // Otherwise, add the enchantment with a count of 1
            enchantments.put(enchantment, 1);
        }

        System.out.println("Collected enchantment: " + enchantment.getClass().getSimpleName());
    }

    public int getLifeCount() {
        return lifeCount;
    }

    public void setLifeCount(int lifeCount) {
        this.lifeCount = lifeCount;
        notifyObservers();
    }
    /**
     * Adds a specified enchantment to the player's inventory.
     * If the enchantment already exists, its count is incremented; otherwise, it is added with an initial count of 1.
     *
     * @param enchantment the enchantment to be added to the inventory
     */
    //kubilay bunları hem senin kullanman için hem de save functionality için ekledim
    public void addEnchantment(Enchantment enchantment) {
        //if enchantment is already in the hashmap,increase the count
        // else add it to the hashmap with count 1
        if(enchantments.containsKey(enchantment)){
            enchantments.put(enchantment,enchantments.get(enchantment)+1);
        }
        else{
            enchantments.put(enchantment,1);
        }

    }
    public void removeEnchantment(Enchantment enchantment) {
        enchantments.remove(enchantment);
    }
    public HashMap<Enchantment,Integer> getEnchantments() {
        return enchantments;
    }

    public Point getPosition() {
        return position;
    }

    //added for arrow coordinates calculation related to player
    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setDirection(int direction) {
        this.playerDir = direction;
        this.moving = true; // Movement starts when direction is set
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void addObserver(HealthObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(HealthObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (HealthObserver observer : observers) {
            observer.onHealthChanged(lifeCount, lifeCount);
        }
    }

    private void loadPlayerImage() {
        try (InputStream is = getClass().getResourceAsStream("/assets/images/player.png")) {
            if (is != null) {
                playerImage = ImageIO.read(is);
            } else {
                System.err.println("Player image not found at /assets/images/player.png");
            }
        } catch (IOException e) {
            System.err.println("Error loading player image: " + e.getMessage());
        }
    }

    @Override
    public void update() {
        updatePosition();
    }

    @Override
    public void render(Graphics g) {
        if (playerImage != null) {
            g.drawImage(playerImage, position.x, position.y, PLAYER_SIZE, PLAYER_SIZE, null);
        } else {
            g.setColor(Color.RED); // Fallback to a red square if the image is missing
            g.fillRect(position.x, position.y, PLAYER_SIZE, PLAYER_SIZE);
        }
    }
    /**
     * Updates the player's position based on the current movement direction and speed.
     * Ensures the player's position remains within the game boundaries.
     * Resets the moving state after updating the position.
     */
    public void updatePosition() {
        if (moving) {
            int newX = position.x;
            int newY = position.y;
            switch (playerDir) {
                case LEFT:
                    newX -= SPEED;
                    break;
                case UP:
                    newY -= SPEED;
                    break;
                case RIGHT:
                    newX += SPEED;
                    break;
                case DOWN:
                    newY += SPEED;
                    break;
                default:
                    break;
            }
            newX = Math.max(0, Math.min(newX, 16 * PLAYER_SIZE - PLAYER_SIZE));
            newY = Math.max(0, Math.min(newY, 16 * PLAYER_SIZE - PLAYER_SIZE));

            position.setLocation(newX, newY);

            moving = false;
        }
    }

    @Override
    public void interact() {
        System.out.println("Player is interacting...");
    }

    public void incrementLives(int livesIncrement) {
        setLifeCount(this.lifeCount + livesIncrement);
    }

    public void decrementLives(int livesDecrement) {
        setLifeCount(Math.max(0, this.lifeCount - livesDecrement));
    }
}