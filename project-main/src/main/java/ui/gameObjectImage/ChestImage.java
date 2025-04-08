/**
 * Responsible for managing and rendering the visual representation of Chest objects.
 * Loads chest images and draws them on the game grid at the specified positions.
 */

package ui.gameObjectImage;

import domain.gameObjects.Chest;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class ChestImage extends GameEntityImage {
    private static final HashMap<Class<? extends Chest>, Image> chestImages = new HashMap<>();
    static {
        chestImages.put(Chest.class, loadImage("../../assets/images/chest.png"));
    }
    private static Image loadImage(String path) {
        return new ImageIcon(ChestImage.class.getResource(path)).getImage();
    }
    public static Image getImageForChest(Chest chest) {
        // Default image handling
        return chestImages.getOrDefault(chest.getClass(), loadImage("../../assets/images/default_chest.png"));
    }
    @Override
    public ImageIcon getImageForEntity(Object entity) {
        if (entity instanceof Chest) {
            return new ImageIcon(getImageForChest((Chest) entity));
        }
        return new ImageIcon(loadImage("../../assets/images/default_chest.png"));
    }
}

