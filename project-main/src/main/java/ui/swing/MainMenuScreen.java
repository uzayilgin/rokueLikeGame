/**
 * The `MainMenuScreen` class provides a visually engaging main menu for the game.
 * It features animated hero and monster images, a dynamic background, and styled buttons for navigation.
 * This serves as the entry point for starting a new game, accessing saved games, viewing help, or exiting the game.
 */

package ui.swing;
import javax.swing.*;
import java.awt.*;

public class MainMenuScreen implements GameView {
    private JPanel mainPanel;
    private JButton startButton;
    private JButton helpButton;
    private JButton exitButton;
    private JButton savedGamesButton;

    private Image backgroundImage;
    private Image heroImage;
    private Image monsterImage;

    private int heroX;
    private int heroY;
    private int heroWidth = 150;
    private int heroHeight = 150;

    private int monsterX;
    private int monsterY;
    private int monsterWidth = 150;
    private int monsterHeight = 150;
    private boolean monsterMoving = false;
    private long startTime;
    private long delayForMonster = 2000;

    private int moveSpeed = 3;
    private double scaleDecrease = 0.997;;

    public MainMenuScreen() {

    }
    /**
     * Initializes the main menu screen by setting up the background image, hero and monster animations,
     * and layout for the title and buttons.
     */
    @Override
    public void initialize() {
        backgroundImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("../../assets/images/background.jpg"));
        heroImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("../../assets/images/player.png"));
        monsterImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("../../assets/images/fighter.png"));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        heroX = (int)(screenSize.width/2 - heroWidth/2 - 100);
        heroY = (int)(screenSize.height - heroHeight - 50);

        monsterX = heroX;
        monsterY = heroY;

        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }

                if (heroImage != null) {
                    g.drawImage(heroImage, heroX, heroY, heroWidth, heroHeight, this);
                }

                if (monsterImage != null && monsterMoving) {
                    g.drawImage(monsterImage, monsterX, monsterY, monsterWidth, monsterHeight, this);
                }
            }
        };

        mainPanel.setOpaque(true);
        mainPanel.setLayout(new GridBagLayout());

        startTime = System.currentTimeMillis();

        Timer timer = new Timer(50, e -> {
            long currentTime = System.currentTimeMillis();

            heroY -= moveSpeed;
            heroWidth = (int)(heroWidth * scaleDecrease);
            heroHeight = (int)(heroHeight * scaleDecrease);

            if (!monsterMoving && (currentTime - startTime) > delayForMonster) {
                monsterMoving = true;
            }

            if (monsterMoving) {
                monsterY -= moveSpeed;
                monsterWidth = (int)(monsterWidth * scaleDecrease);
                monsterHeight = (int)(monsterHeight * scaleDecrease);
            }

            mainPanel.repaint();
        });
        timer.start();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Rokue-Like");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 60));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        startButton = createStyledButton("Start New Game");
        savedGamesButton = createStyledButton("Saved Games");
        helpButton = createStyledButton("Help");
        exitButton = createStyledButton("Exit");

        gbc.gridy = 1;
        gbc.insets = new Insets(10,10,10,10);
        buttonPanel.add(startButton, gbc);
        gbc.gridy = 2;
        buttonPanel.add(savedGamesButton, gbc);
        gbc.gridy = 3;
        buttonPanel.add(exitButton, gbc);
        gbc.gridy = 4;
        buttonPanel.add(helpButton, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(50,20,20,20);
        mainPanel.add(buttonPanel, gbc);

        mainPanel.setFocusable(true);
        mainPanel.requestFocusInWindow();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 24));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50,50,50,180));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70,70,70,200));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50,50,50,180));
            }
        });

        return button;
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

    public JButton getStartButton() {
        return startButton;
    }
    public JButton getSavedGamesButton() {
        return savedGamesButton;
    }

    public JButton getHelpButton() {
        return helpButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}
