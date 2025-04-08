/**
 * Responsible for managing and rendering the visual representation of Rune objects.
 * Loads rune images and draws them on the game grid at the specified positions.
 */

package ui.gameObjectImage;

import domain.gameObjects.Rune;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Objects;

public class RuneImage extends GameEntityImage {
    private static final HashMap<Class<? extends Rune>, Image> runeImages = new HashMap<>();

    static {
        // Load the default rune image
        runeImages.put(Rune.class, loadImage("../../assets/images/rune.png"));
    }

    // Method to load the image from the given path
    private static Image loadImage(String path) {
        return new ImageIcon(Objects.requireNonNull(RuneImage.class.getResource(path))).getImage();
    }

    // Get the image for the given rune
    public static Image getImageForRune(Rune rune) {
        // Default image handling based on the class of the rune
        switch (rune.getRuneType()) {
            case FIRE:
                return runeImages.computeIfAbsent(rune.getClass(), k -> loadImage("../../assets/images/fireRune.png"));
            case WATER:
                return runeImages.computeIfAbsent(rune.getClass(), k -> loadImage("../../assets/images/waterRune.png"));
            case EARTH:
                return runeImages.computeIfAbsent(rune.getClass(), k -> loadImage("../../assets/images/earthRune.png"));
            case AIR:
                return runeImages.computeIfAbsent(rune.getClass(), k -> loadImage("../../assets/images/airRune.png"));
        }
        return runeImages.getOrDefault(rune.getClass(), loadImage("../../assets/images/rune.png"));
    }

    // Overridden method to get the image for an entity (e.g., rune)
    @Override
    public ImageIcon getImageForEntity(Object entity) {
        if (entity instanceof Rune) {
            return new ImageIcon(getImageForRune((Rune) entity));  // Correct class usage
        }
        return new ImageIcon(loadImage("../../assets/images/rune.png")); // Fallback image
    }
}
