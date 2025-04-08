/**
 * Responsible for managing and rendering the visual representation of Player objects.
 * Loads player images and draws them on the game grid at the specified positions.
 */

package ui.gameObjectImage;

import domain.gameObjects.Player;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class PlayerImage extends GameEntityImage {
    private static final HashMap<Class<? extends Player>, Image> playerImages = new HashMap<>();

    static {
        playerImages.put(Player.class, loadImage("/assets/images/player.png"));
    }

    private static Image loadImage(String path) {
        try {
            return new ImageIcon(PlayerImage.class.getResource(path)).getImage();
        } catch (Exception e) {
            System.err.println("Failed to load image at path: " + path);
            return new ImageIcon(PlayerImage.class.getResource("/assets/images/default_player.png")).getImage();
        }
    }

    public static Image getImageForPlayer(Player player) {
        return playerImages.getOrDefault(player.getClass(), loadImage("/assets/images/default_player.png"));
    }

    @Override
    public ImageIcon getImageForEntity(Object entity) {
        if (entity instanceof Player) {
            return new ImageIcon(getImageForPlayer((Player) entity));
        }
        return new ImageIcon(loadImage("/assets/images/default_player.png"));
    }
}
