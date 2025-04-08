/**
 * The `ImageBorder` class provides a custom border implementation using an image.
 * It extends `AbstractBorder` to allow for flexible, image-based border rendering around components.
 */

package ui.swing;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import javax.swing.*;

public class ImageBorder extends AbstractBorder {

    private final ImageIcon img;

    public ImageBorder(String imagePath) {
        this.img = new ImageIcon(getClass().getResource(imagePath));
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(img.getImage(), x, y, width, height, null);
    }

    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(5, 5, 5, 5);
    }
}
