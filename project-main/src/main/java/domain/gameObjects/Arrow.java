/**
 * The Arrow class represents an arrow object in the game, fired by an ArcherMonster or similar entity.
 * The arrow moves toward a specified target point and interacts when it reaches the target.
 */
package domain.gameObjects;

import ui.gameObjectImage.ArrowImage;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Arrow extends GameObject {
    private int x;
    private int y;
    private int direction;
    private int speed = 1;
    private int targetX;
    private int targetY;


    public Arrow(int x, int y, int targetX, int targetY) {
        super(x, y);
        this.targetX = targetX;
        this.targetY = targetY;
    }
    public void interact() {
    }

    public boolean hasReachedTarget() {
        return x == targetX && y == targetY;
    }

    public void moveArrow() {
        int deltaX = targetX - x;
        int deltaY = targetY - y;

        // Calculate the magnitude (distance) to the target
        double magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if (magnitude == 0) return;  // Arrow has reached target (don't move)

        // Normalize direction vector (scale to unit length)
        double unitX = deltaX / magnitude;
        double unitY = deltaY / magnitude;

        // Move the arrow by a fixed speed
        int nextX = x + (int)(unitX * speed);
        int nextY = y + (int)(unitY * speed);

        // Update position if no collision
        x = nextX;
        y = nextY;

        System.out.println("Arrow moved to: (" + x + ", " + y + ")");

        // Optionally interact if the target is reached
        if (hasReachedTarget()) {
            interact();
        }
    }

    public boolean isOutOfBounds(int width, int height) {
        return x < 0 || x > width || y < 0 || y > height;
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public void render(Graphics g, ArrowImage arrowImage, int cellSize) {
        if (arrowImage != null) {
            arrowImage.render(g, this, 50);
        } else {
            System.err.println("Arrow image not loaded properly");
        }
    }

}
