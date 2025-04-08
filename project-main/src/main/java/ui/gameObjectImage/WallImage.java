/**
 * Responsible for managing and rendering the visual representation of Wall objects.
 * Loads wall images and draws them on the game grid at the specified positions.
 */

package ui.gameObjectImage;

import domain.gameObjects.Wall;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class WallImage extends GameEntityImage {
    private static final HashMap<Class<? extends Wall>, Image> wallImages = new HashMap<>();
    static {
        wallImages.put(Wall.class, loadImage("../../assets/images/wall.png"));
    }
    private static Image loadImage(String path) {
        return new ImageIcon(WallImage.class.getResource(path)).getImage();
    }
    public static Image getImageForWall(Wall wall) {
        // Default image handling
        return wallImages.getOrDefault(wall.getClass(), loadImage("../../assets/images/default_wall.png"));
    }
    @Override
    public ImageIcon getImageForEntity(Object entity) {
        if (entity instanceof Wall) {
            return new ImageIcon(getImageForWall((Wall) entity));
        }
        return new ImageIcon(loadImage("../../assets/images/default_wall.png"));
    }
}
