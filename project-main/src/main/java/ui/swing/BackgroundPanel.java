/**
 * A custom `JPanel` that displays a background image and an optional grid overlay.
 * Allows dynamic loading and updating of the background image, as well as rendering grid lines.
 */

package ui.swing;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    // Constructor to load background image
    public BackgroundPanel(String imagePath) {
        try {
            URL imgURL = getClass().getClassLoader().getResource(imagePath);
            if (imgURL != null) {
                backgroundImage = ImageIO.read(imgURL);
            } else {
                System.err.println("Background image not found: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to set the background image
    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
        repaint();
    }
    /**
     * Custom painting method to render the background image and grid overlay.
     * Scales the background image to fit the panel size and draws grid lines over it.
     * @param g The `Graphics` object used for rendering.
     */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Scale the background image to fit the panel size
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Draw grid lines
        int rows = 16;
        int cols = 16;
        int cellWidth = getWidth() / cols;
        int cellHeight = getHeight() / rows;

        // Set grid line color
        g.setColor(Color.GRAY);

        // Draw horizontal grid lines
        for (int row = 0; row <= rows; row++) {
            g.drawLine(0, row * cellHeight, getWidth(), row * cellHeight);
        }

        // Draw vertical grid lines
        for (int col = 0; col <= cols; col++) {
            g.drawLine(col * cellWidth, 0, col * cellWidth, getHeight());
        }
    }

    @Override
    public Dimension getPreferredSize() {
        // Provide the preferred size based on grid size
        return new Dimension(800, 800); // Adjust this based on your needs
    }
}
