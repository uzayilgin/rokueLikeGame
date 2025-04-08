/**
 * The `InventoryPanel` class is responsible for displaying the player's inventory in a visual format.
 * It extends `JPanel` and dynamically renders the inventory items based on the player's collected enchantments.
 *
 * - Displays each enchantment as an image and text (name and quantity).
 * - Supports real-time updates through `updateInventory()`.
 */

package ui.swing;

import domain.controllers.EnchantmentController;
import domain.gameObjects.Enchantment;
import domain.gameObjects.Player;
import ui.gameObjectImage.EnchantmentImage;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class InventoryPanel extends JPanel {
    private final Player player;


    public InventoryPanel(Player player) {
        this.player = player;
        setPreferredSize(new Dimension(200, 300)); // Adjust as needed
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Vertical layout

        // You can add an empty panel at the end to provide space before inventory items (if needed)
        add(Box.createVerticalStrut(10));  // Adds space between colored panels and inventory
    }

    // Helper method to add a colored panel to the InventoryPanel
    private void addColoredPanel(Color color) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(180, 50)); // Adjust panel size as needed
        panel.setBackground(color); // Set background color
        add(panel); // Add the colored panel to the layout
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Get the player's inventory, which is a Map<Enchantment, Integer>
        Map<Enchantment, Integer> inventory = player.getEnchantments();

        // Draw title
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Inventory", 10, 20);

        // Set position for the first item
        int y = 40; // Start drawing below the title
        for (Map.Entry<Enchantment, Integer> entry : inventory.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int quantity = entry.getValue();

            // Get the enchantment image using EnchantmentImage class
            Image enchantmentImage = EnchantmentImage.getImageForEnchantment(enchantment);
            if (enchantmentImage != null) {
                g.drawImage(enchantmentImage, 10, y, 50, 50, this); // Image size 50x50
            }

            // Draw the enchantment name and quantity next to the image
            String text = enchantment.getClass().getSimpleName() + ": " + quantity;
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString(text, 70, y + 25); // Positioning the text next to the image

            // Adjust the position for the next item
            y += 60; // Move down for the next item
        }
    }
    public void updateInventory() {
        revalidate();
        repaint();
    }
}