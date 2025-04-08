/**
 * Responsible for managing and rendering the visual representation of Monster objects.
 * Loads monster images and draws them on the game grid at the specified positions.
 */

package ui.gameObjectImage;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.util.HashMap;
import domain.gameObjects.Monster;
import domain.gameObjects.ArcherMonster;
import domain.gameObjects.FighterMonster;
import domain.gameObjects.WizardMonster;

public class MonsterImage extends GameEntityImage {
    private static final HashMap<Class<? extends Monster>, Image> monsterImages = new HashMap<>();

    static {
        // Preload images
        monsterImages.put(ArcherMonster.class, loadImage("../../assets/images/archerWithBow.png"));
        monsterImages.put(FighterMonster.class, loadImage("../../assets/images/fighter.png"));
        monsterImages.put(WizardMonster.class, loadImage("../../assets/images/wizard.png"));
    }

    private static Image loadImage(String path) {
        return new ImageIcon(MonsterImage.class.getResource(path)).getImage();
    }

    // Get the image corresponding to a monster type
    public static Image getImageForMonster(Monster monster) {
        return monsterImages.getOrDefault(monster.getClass(), loadImage("../../assets/images/default_monster.png"));
    }

    @Override
    public ImageIcon getImageForEntity(Object entity) {
        if (entity instanceof Monster) {
            return new ImageIcon(getImageForMonster((Monster) entity));
        }
        return new ImageIcon(loadImage("../../assets/images/default_monster.png"));
    }
}