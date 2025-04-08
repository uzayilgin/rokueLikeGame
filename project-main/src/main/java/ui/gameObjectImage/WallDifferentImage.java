/**
 * Responsible for managing and rendering the visual representation of DifferentWall objects.
 * Loads different Wall images and draws them on the game grid at the specified positions.
 */

package ui.gameObjectImage;
import domain.gameObjects.WallDifferent;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Objects;

public class WallDifferentImage extends GameEntityImage{
    private static final HashMap<Class<? extends WallDifferentImage>, Image> wallDifferentImages = new HashMap<>();
    static {
        wallDifferentImages.put(WallDifferentImage.class, loadImage("../../assets/images/wallDiff.png"));
    }
    private static Image loadImage(String path) {
        return new ImageIcon(Objects.requireNonNull(WallDifferentImage.class.getResource(path))).getImage();
    }
    public static Image getImageForWallDifferent(WallDifferent wallDifferent) {
        // Default image handling
        return wallDifferentImages.getOrDefault(wallDifferent.getClass(), loadImage("../../assets/images/wallDiff.png"));
    }
    @Override
    public ImageIcon getImageForEntity(Object entity) {
        if (entity instanceof WallDifferent) {
            return new ImageIcon(getImageForWallDifferent((WallDifferent) entity));
        }
        return new ImageIcon(loadImage("../../assets/images/wallDiff.png"));
    }
}
