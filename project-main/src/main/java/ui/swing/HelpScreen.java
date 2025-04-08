/**
 * The `HelpScreen` class represents a UI component that displays detailed instructions and information about the game.
 */

package ui.swing;
import javax.swing.*;
import java.awt.*;

public class HelpScreen implements GameView {
    private JPanel mainPanel;
    private Image backgroundImage;
    private JButton closeButton;

    String helpText =  " Rokue-Like is an adventure game aiming to find hidden runes in the EARTH, AIR, WATER, and FIRE halls and reach the exit before the time expires.\n" +
            "\n" +
            "  - GAME CONTROLS:\n\n" +
            " Movement: Use the arrow keys (↑, ↓, ←, →).\n" +
            " Click: Collect the objects next to you with the left mouse click.\n" +
            "\n" +
            "  - GAME FLOW:\n\n" +
            " 1. BUILD MODE:\n" +
            " Before starting the game, you must place the objects in the halls.\n" +
            " Minimum number of objects:\n" +
            "\n" +
            "  * Earth Hall: At least 6 objects\n" +
            "  * Air Hall: At least 9 objects\n" +
            "  * Water Hall: At least 13 objects\n" +
            "  * Fire Hall: At least 17 objects\n" +
            "\n" +
            " 2. PLAY MODE:\n" +
            " The time set for each hall depends on the number of objects in there (5 seconds for each object).\n" +
            " If you find the rune, the exit door of the hall will open.\n" +
            " When you find the rune, you can move on to the next hall.\n" +
            " If you find all 4 runes, you win the game.\n" +
            " If you cannot find the rune before the time runs out, the game ends.\n" +
            "\n" +
            "  - FINDING RUNES:\n\n" +
            " Runes are under an object.\n" +
            " When you are next to an object, you can click on the object with your mouse, and if the rune is there, it will appear.\n" +
            " When the rune is found, the door will open, and you will hear a sound effect.\n" +
            "\n" +
            "  - MONSTERS:\n\n" +
            " A random monster can appear in the hall every 8 seconds:\n" +
            "\n" +
            " 1. Archer Monster:\n" +
            " Shoots arrows every second.\n" +
            " If you get within 4 squares, you lose health.\n" +
            " If you have \"Cloak of Protection,\" the archer cannot see you.\n" +
            "\n" +
            " 2. Fighter Monster:\n" +
            " Stabs and kills when it comes to you.\n" +
            " You are lucky. It moves randomly and cannot see you from a distance.\n" +
            " Can be tricked with \"Luring Gem.\" You can throw the Luring Gem in a certain direction and pull the monster to that direction.\n" +
            "\n" +
            " 3. Wizard Monster:\n" +
            " It teleports the rune's location under a random object every 5 seconds.\n" +
            " It does not move.\n" +
            "\n" +
            " You have 3 lives at the beginning. If your health is zero, the game is over.\n" +
            " Survive by collecting items that provide additional health or protection.\n" +
            "\n" +
            "  - ENCHANTMENTS:\n" +
            "\n" +
            " Spawns at a random location every 12 seconds. If you do not collect it, it disappears after 6 seconds.\n" +
            " Click on it to collect if you are next to it.\n" +
            "\n" +
            " 1. Extra Time:\n" +
            "It instantly gives 5 seconds extra time when collected.\n" +
            "\n" +
            " 2. Reveal:\n" +
            " It goes to the bag. When used with the R key, it marks a 4x4 area where the rune is located. Remember, it is visible for 10 seconds.\n" +
            "\n" +
            " 3. Cloak of Protection:\n" +
            " It goes to the bag. When you press the P key, the Archer Monster cannot see you for 20 seconds.\n" +
            "\n" +
            " 4. Luring Gem:\n" +
            " It goes to the bag. After pressing the B key, you select the direction to throw with the A, D, W, S keys (←, →, ↑, ↓). It tricks the Fighter Monster and pulls it in that direction.\n" +
            "\n" +
            " 5. Extra Life:\n" +
            " Your health increases by 1 when collected.\n" +
            "\n" +
            " - INTERFACE:\n" +
            "\n" +
            " - On the screen you can see the following:\n" +
            "  * Remaining time\n" +
            "  * Name of the hall you are currently in\n" +
            "  * Remaining number of lives\n" +
            "  * Items in your bag and their numbers\n" +
            "  * Pause / Resume button: You can pause and continue the game.\n" +
            "  * Exit button: Exits the game and returns to the main menu.\n" +
            "\n" +
            "  - WINNING AND LOSING:\n\n" +
            " - If you find all the runes and exit the Fire Hall, you win the game.\n" +
            " - If you run out of lives or cannot find the rune before the time runs out, you lose the game.\n" +
            "\n" +
            "                                                                                                            GOOD LUCK AND HAVE FUN";

    public HelpScreen() {

    }

    @Override
    public void initialize() {
        backgroundImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("../../assets/images/b2.jpg"));

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
        mainPanel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("GAME INSTRUCTIONS", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 48));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea(helpText);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);
        textArea.setForeground(Color.WHITE);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 20));

        JScrollPane scrollPane = new JScrollPane(textArea) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setOpaque(false);
                getViewport().setOpaque(false);
            }
        };
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        bottomPanel.setOpaque(false);
        closeButton = createStyledButton("Close");
        bottomPanel.add(closeButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

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

    public JButton getCloseButton() {
        return closeButton;
    }
}
