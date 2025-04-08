/**
 * Responsible for managing and rendering the visual representation of GameEntity objects.
 * Loads GameEntity images and draws them on the game grid at the specified positions.
 */

package ui.gameObjectImage;

import javax.swing.*;
import java.awt.*;

public abstract class GameEntityImage {
    // Abstract method to get the ImageIcon for rendering
    public abstract ImageIcon getImageForEntity(Object entity);

    // Common rendering method
    public void render(Graphics g, Object entity, int x, int y) {
        ImageIcon icon = getImageForEntity(entity);
        g.drawImage(icon.getImage(), x * 32, y * 32, 32, 32, null); // Scale to 32x32 cells
    }
}
