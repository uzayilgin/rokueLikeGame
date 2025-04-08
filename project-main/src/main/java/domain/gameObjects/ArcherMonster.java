/**
 * The ArcherMonster class represents an archer-type monster in the game.
 * This monster can shoot arrows at the player if within range but does not move.
 */
package domain.gameObjects;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.IOException;
import domain.gameCore.GameInitialization;
import domain.utilities.Constants;
import technicalServices.logging.LogManager;

public class ArcherMonster extends Monster {
    private Image archerSprite;
    private List<Arrow> arrows;
    private long lastShotTime;
    private static final int SHOOT_DELAY = 1000;
    private static final int SHOOT_RANGE = 400;
    private GameInitialization game;
    private static final int SPRITE_WIDTH = 32;
    private static final int SPRITE_HEIGHT = 32;
    /**
     * Constructs an ArcherMonster at the specified position.
     *
     * @param x the x-coordinate of the monster
     * @param y the y-coordinate of the monster
     */
    public ArcherMonster( int x, int y) {
        super(x, y);
        LogManager.logInfo("Archer Monster created -> " + x + ", " + y + " [from class: ArcherMonster, method: ArcherMonster]");
        System.out.println("Archer Monster created -> " + x + ", " + y);
        this.arrows = new ArrayList<>();
        this.lastShotTime = 0;

        try {
            archerSprite = ImageIO.read(getClass().getResource("/assets/images/archer2x.png"));
        } catch (IOException e) {
            LogManager.logError("Error loading archer sprite -> " + e.getMessage() + " [from class: ArcherMonster, method: ArcherMonster]");
            System.err.println("Error loading archer sprite -> " + e.getMessage());
        }
    }

    @Override
    public void attack(Player player) {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - lastShotTime >= SHOOT_DELAY)
                && Monster.calculateDistance(getPosition(), player.getPosition()) <= SHOOT_RANGE) {
//           Need further implementation and inspection therefore I am not removing the code below
//            Point playerPos = player.getPosition();
//            Point direction = new Point(playerPos.x - getPosition().x, playerPos.y - getPosition().y);
//
//            double magnitude = Math.sqrt(direction.x * direction.x + direction.y * direction.y);
//            direction.x /= magnitude;
//            direction.y /= magnitude;
//            Point currentArcherPos = new Point(getPosition().x, getPosition().y);
//            Arrow arrow = new Arrow(currentArcherPos.x, currentArcherPos.y, player.getX(), player.getY());
//
//            arrows.add(arrow);
//            System.out.println("Arrow fired from: " + currentArcherPos.x + ", " + currentArcherPos.y);
//            lastShotTime = currentTime;
        }
    }

    @Override
    public void move(ArrayList<Point> emptyLocations) {
        // Archer doesn't move
    }

    @Override
    public void interact() {
        // Interaction logic can be added if needed
    }

    @Override
    public void render(Graphics g) {
        if (archerSprite != null) {
            g.drawImage(archerSprite, (int) getPosition().getX(), (int) getPosition().getY(), SPRITE_WIDTH, SPRITE_HEIGHT, null);
        }

//        for(Arrow arrow : arrows) {
//            arrow.render(g);
//        }
    }
    @Override
    public Constants.GameObjectsInHall getType() {
        return Constants.GameObjectsInHall.ARCHER;
    }
}
