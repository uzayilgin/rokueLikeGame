/**
 * Represents the UI for the Water Hall screen in the game.
 * Displays a themed background and a welcome message for the hall.
 * Provides layout for additional UI elements or game components.
 */

package ui.swing;

import javax.swing.*;
import java.awt.*;

public class WaterHallScreen implements GameView {
    private JPanel mainPanel;
    private Image backgroundImage;

    public WaterHallScreen() {
    }

    @Override
    public void initialize() {
        backgroundImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("../../assets/images/waterHall.png"));

        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        mainPanel.setOpaque(true);
        mainPanel.setLayout(new GridBagLayout());

        JLabel welcomeMessage = new JLabel("Welcome to the Water Hall!");
        welcomeMessage.setFont(new Font("Serif", Font.BOLD, 48));
        welcomeMessage.setForeground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        mainPanel.add(welcomeMessage, gbc);

        mainPanel.setFocusable(true);
        mainPanel.requestFocusInWindow();
    }

    @Override
    public void render() {
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    @Override
    public void teardown() {
        mainPanel.removeAll();
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }
}