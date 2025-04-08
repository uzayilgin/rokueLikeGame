/**
 * Responsible for managing and rendering the visual representation of Life objects.
 * Loads life images and draws them on the game grid at the specified positions.
 */

package ui.gameObjectImage;

import javax.swing.*;
import java.awt.*;

public class LifeImage {
    private static final String HEART_IMAGE_PATH = "../../assets/images/heart4x.png";
    private Image heartImage;

    public LifeImage() {
        loadHeartImage();
    }

    // Load the heart image
    private void loadHeartImage() {
        ImageIcon heartIcon = new ImageIcon(HEART_IMAGE_PATH);
        heartImage = heartIcon.getImage();
    }

    // Render the heart image at a specified location
    public void render(Graphics g, int x, int y, int width, int height) {
        if (heartImage != null) {
            g.drawImage(heartImage, x, y, width, height, null);
        }
    }
}
