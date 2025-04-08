/**
 * Responsible for managing and rendering the visual representation of Block objects.
 * Loads block images and draws them on the game grid at the specified positions.
 */

package ui.gameObjectImage;

import domain.gameObjects.Block;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Objects;

public class BlockImage extends GameEntityImage{
    private static final HashMap<Class<? extends Block>, Image> blockImages = new HashMap<>();
    static {
        blockImages.put(Block.class, loadImage("../../assets/images/block.png"));
    }
    private static Image loadImage(String path) {
        return new ImageIcon(Objects.requireNonNull(BlockImage.class.getResource(path))).getImage();
    }
    public static Image getImageForBlock(Block block) {
        // Default image handling
        return blockImages.getOrDefault(block.getClass(), loadImage("../../assets/images/block.png"));
    }
    @Override
    public ImageIcon getImageForEntity(Object entity) {
        if (entity instanceof Block) {
            return new ImageIcon(getImageForBlock((Block) entity));
        }
        return new ImageIcon(loadImage("../../assets/images/block.png"));
    }
}
