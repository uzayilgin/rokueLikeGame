/**
 * A UI component for displaying the player's health using heart icons.
 * Implements the HealthObserver interface to update the health display in response to changes in the player's life count.
 * Displays messages when the player gains or loses health and handles "game over" scenarios.
 */

package ui.swing;

import domain.controllers.GameController;
import domain.gameObjects.Player;
import domain.observers.HealthObserver;
import javax.swing.*;
import java.awt.*;

public class HealthHeartDisplay implements HealthObserver {
    private JPanel healthPanel;
    private Player player;
    private GameController controller;// Reference to the Player
    private JPanel messagePanel;
    private int lifePanelTrack;

    public HealthHeartDisplay(Player player, GameController controller) {
        this.player = player;
        this.controller = controller;// Assign the player reference
        healthPanel = new JPanel();
        healthPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        messagePanel = new JPanel();
        messagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Register this display as an observer of the player
        this.player.addObserver(this);

        // Immediately update the display with the initial health count
        onHealthChanged(player.getLifeCount(), player.getLifeCount());
    }

    public JPanel getPanel() {
        JPanel panel = new JPanel(new BorderLayout());  // Main panel with BorderLayout
        panel.add(healthPanel, BorderLayout.NORTH);     // Add healthPanel to the north
        panel.add(messagePanel, BorderLayout.CENTER);   // Add messagePanel to the center
        return panel;
    }

    /**
     * Updates the health display in response to changes in the player's life count.
     *
     * @param newHealth The updated health value of the player.
     * @param oldHealth The previous health value of the player.
     */

    @Override
    public void onHealthChanged(int newHealth, int oldHealth) {
        //since now we do not have life enchantment the only operation done is decreasing life
        //display a message to the user without the condition to check on if life increased or decreased
        int previousHealth = oldHealth;

        // If the new health is less than the previous health, show the "Ouch!!!" message
        if (newHealth < previousHealth) {
            showMessage("Ouch!!! Player got hit by a monster!");
        } else if (newHealth > previousHealth) {
            showMessage("Yey!!! Player won a life! "); // Clear message if health is not decreased
        }

        lifePanelTrack = 1;
        healthPanel.removeAll(); // Clear old hearts

        // Add hearts for the current life count
        for (int i = 0; i < newHealth; i++) {
            JLabel heartLabel = new JLabel("\u2764"); // Unicode for heart: ❤
            heartLabel.setFont(new Font("Segoe UI Symbol", Font.BOLD, 30));
            heartLabel.setForeground(Color.RED);
            healthPanel.add(heartLabel);
        }

        healthPanel.revalidate();
        healthPanel.repaint();

        // If the player's health is 0, handle the "game over"
        if (player.getLifeCount() == 0) {
            this.isPlayerDead();
        }
    }

    @Override
    public void isPlayerDead() {
        controller.notifyGameOver();
    }
    public void showMessage(String message) {
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Garamond", Font.PLAIN, 20));
        messageLabel.setForeground(new Color(0x7A5937));
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);
    
        messagePanel.removeAll();
        messagePanel.add(messageLabel, gbc);
    
        messagePanel.revalidate();
        messagePanel.repaint();
     
        // Remove the message after a delay
        Timer timer = new Timer(3000, e -> messageLabel.setText(" "));
        timer.setRepeats(false);
        timer.start();
    }
    

}
