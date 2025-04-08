/**
 * Responsible for managing and rendering the visual representation of Arrow objects.
 * Loads arrow images and draws them on the game grid at the specified positions.
 */

package ui.gameObjectImage;

import domain.gameObjects.Arrow;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class ArrowImage {
    private Image arrowImage;

    public ArrowImage() {
        try {
            arrowImage = ImageIO.read(getClass().getResource("../../assets/images/Arrow.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics g, Arrow arrow, int cellSize) {
        Point position = arrow.getPosition();
        int x = position.x * cellSize;
        int y = position.y * cellSize;
        g.drawImage(arrowImage,x,y,cellSize,cellSize,null);
    }

}
