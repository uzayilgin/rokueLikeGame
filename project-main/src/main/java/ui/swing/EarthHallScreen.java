/**
 * Represents the Earth Hall screen in the game, providing a custom background and layout for the Earth-themed hall.
 * Implements the GameView interface to handle initialization, rendering, and teardown of the view.
 */

package ui.swing;

import javax.swing.*;
import java.awt.*;

public class EarthHallScreen implements GameView {
    private JPanel mainPanel;
    private Image backgroundImage;

    public EarthHallScreen() {

    }
    /**
     * Initializes the Earth Hall screen by setting up the background, layout, and components.
     * Loads the background image and displays a centered welcome message.
     */
    @Override
    public void initialize() {
        backgroundImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("../../assets/images/earthHall.png"));

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
        mainPanel.setLayout(new GridBagLayout()); // Center align the components

        // Add the welcome message
        JLabel welcomeMessage = new JLabel("Welcome to the Earth Hall!");
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