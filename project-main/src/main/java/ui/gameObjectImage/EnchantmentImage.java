/**
 * Responsible for managing and rendering the visual representation of Enchantment objects.
 * Loads enchantment images and draws them on the game grid at the specified positions.
 */

package ui.gameObjectImage;

import domain.gameObjects.*;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.util.HashMap;

public class EnchantmentImage extends GameEntityImage {
    // Enum representing different enchantment types
    public enum EnchantmentType {
        CLOAK_OF_PROTECTION,
        LURING_GEM,
        EXTRA_LIFE,
        EXTRA_TIME,
        REVEAL
    }

    // HashMap to store images for different enchantment types
    private static final HashMap<Class<? extends Enchantment>, Image> enchantmentImages = new HashMap<>();
    // here the images are not here yet pls add to assets/images
    static {
        // Preload images for each enchantment class
        enchantmentImages.put(CloakEnchantment.class, loadImage("../../assets/images/protectioncloak.png"));
        enchantmentImages.put(LuringGemEnchantment.class, loadImage("../../assets/images/luringGem.png"));
        enchantmentImages.put(LifeEnchantment.class, loadImage("../../assets/images/heart4x.png"));
        enchantmentImages.put(RevealEnchantment.class, loadImage("../../assets/images/reveal.png"));
        enchantmentImages.put(TimeEnchantment.class, loadImage("../../assets/images/clock.png"));
    }

    /**
     * Load image from the given path
     * @param path Path to the image asset
     * @return Loaded Image
     */
    private static Image loadImage(String path) {
        return new ImageIcon(EnchantmentImage.class.getResource(path)).getImage();
    }

    /**
     * Retrieve the image corresponding to the enchantment type
     * @param enchantment Enchantment object
     * @return Image for the given enchantment type
     */
    public static Image getImageForEnchantment(Enchantment enchantment) {
        //here there is no default image for the enchantment pls add them to assets/images
        return enchantmentImages.getOrDefault(enchantment.getClass(), loadImage("../../assets/images/protectioncloak.png"));


    }
    @Override
    public ImageIcon getImageForEntity(Object entity) {
        if (entity instanceof Enchantment) {
            return new ImageIcon(getImageForEnchantment((Enchantment) entity));
        }
        return new ImageIcon(loadImage("../../assets/images/clock.png"));
    }
}