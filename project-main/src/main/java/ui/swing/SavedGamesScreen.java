/**
 * Represents the Saved Games Screen UI in the game.
 * Displays a list of saved games, and provides options to load, delete, or cancel.
 * Implements `GameView` for easy integration with the overall game architecture.
 */

package ui.swing;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SavedGamesScreen implements GameView {
    private JPanel mainPanel;
    private JList<String> savedGamesList;
    private JButton loadButton;
    private JButton cancelButton;
    private JButton deleteSelectedButton;
    private Image backgroundImage;

    public SavedGamesScreen(ArrayList<String> savedGames) {

        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Saved Games");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 60));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 20, 30, 20);
        mainPanel.add(titleLabel, gbc);


        backgroundImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("../../assets/images/background2.jpg"));

        savedGamesList = new JList<>(savedGames.toArray(new String[0]));
        savedGamesList.setCellRenderer(new CustomListCellRenderer());
        savedGamesList.setOpaque(false);
        savedGamesList.setSelectionBackground(new Color(0, 0, 0, 100));
        savedGamesList.setPreferredSize(new Dimension(200, 120));

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());
        listPanel.add(new JScrollPane(savedGamesList), BorderLayout.CENTER);
        listPanel.setOpaque(false);
        listPanel.setPreferredSize(new Dimension(250, 150));

        gbc.gridy = 1;
        gbc.insets = new Insets(50, 20, 20, 20);
        mainPanel.add(listPanel, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);

        loadButton = createStyledButton("Load Game");
        cancelButton = createStyledButton("Cancel");
        deleteSelectedButton = createStyledButton("Delete Selected");

        buttonPanel.add(loadButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(deleteSelectedButton);
        gbc.gridy = 2;
        gbc.insets = new Insets(30, 20, 50, 20);
        mainPanel.add(buttonPanel, gbc);
    }

    @Override
    public void initialize() {}

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

    public JButton getLoadButton() {
        return loadButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JButton getDeleteSelectedButton() {
        return deleteSelectedButton;
    }

    public void setSavedGames(List<String> savedGames) {
        savedGamesList.setListData(savedGames.toArray(new String[0]));
    }

    public String getSelectedGame() {
        return savedGamesList.getSelectedValue();
    }

    private class CustomListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (isSelected) {
                c.setBackground(new Color(0, 0, 0, 100));
                c.setForeground(Color.WHITE);
            } else {
                c.setBackground(new Color(0, 0, 0, 0));
                c.setForeground(Color.BLACK);
            }

            return c;
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 24));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 50, 50, 180));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 70, 70, 200));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 50, 50, 180));
            }
        });
        return button;
    }
}
