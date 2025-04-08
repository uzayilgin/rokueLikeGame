/**
 * The `PlayMode` class manages the user interface and gameplay elements during the play phase of the game.
 * It provides functionality for rendering the game grid, handling player interactions, and updating game state.
 */

package ui.swing;

import domain.behaviors.HallStrategy;
import domain.controllers.EnchantmentController;
import domain.controllers.GameController;
import domain.controllers.PlayerController;
import domain.gameObjects.Arrow;
import domain.gameObjects.Enchantment;
import domain.gameObjects.GameObject;
import domain.gameObjects.Player;
import domain.observers.HallObserver;
import domain.observers.HealthObserver;
import technicalServices.input.InputHandler;
import ui.gameObjectImage.ArrowImage;
import ui.gameObjectImage.GameEntityImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Map;

public class PlayMode implements GameView, HallObserver {
    private JLabel saveDateLabel;
    private JPanel playPanel;
    private JPanel[][] gridPanels;
    private GameEntityImage gameEntityImage;
    private GameController controller;  
    private PlayerController playerController;
    private EnchantmentController enchantmentController;
    private InputHandler inputHandler;
    private Image gridImage;
    private Rectangle highlightRectangle;
    private Player player;
    private HealthHeartDisplay healthHeartDisplay;

    private JPanel controlPanel;
    private JButton pauseButton;
    private JButton resumeButton;
    private JButton quitButton;
    private JButton saveGameButton;
    private HallStrategy hallStrategy;
    private InventoryPanel inventoryPanel;

    private KeyAdapter temporaryKeyListener;

    public void addTemporaryKeyListener(KeyAdapter keyAdapter) {
        if (temporaryKeyListener != null) {
            playPanel.removeKeyListener(temporaryKeyListener);
        }
        temporaryKeyListener = keyAdapter;
        playPanel.addKeyListener(temporaryKeyListener);
    }

    public void removeTemporaryKeyListener(KeyAdapter keyAdapter) {
        if (temporaryKeyListener == keyAdapter) {
            playPanel.removeKeyListener(temporaryKeyListener);
            temporaryKeyListener = null;
        }
    }

    public PlayMode(GameController controller, PlayerController playerController, HallStrategy hallStrategy) {
        this.controller = controller;
        this.playerController = playerController;
        this.inputHandler = new InputHandler(controller, playerController, this);
        this.hallStrategy = hallStrategy;
        controller.getGameState().getHall().addListener(this);

        this.player = controller.getPlayerObject();
        if (this.player != null) {
            this.player.addObserver(new HealthHeartDisplay(player, controller));
        }
        this.player.addObserver(new HealthObserver() {
            @Override
            public void onHealthChanged(int newHealth, int oldHealth) {
                if (newHealth == 0) {
                    healthHeartDisplay.isPlayerDead();
                }
            }

            @Override
            public void isPlayerDead() {
                healthHeartDisplay.isPlayerDead();

            }
        });

        try {
            URL gridImgURL = getClass().getClassLoader().getResource("assets/images/grid.png");
            if (gridImgURL != null) {
                gridImage = ImageIO.read(gridImgURL);
            } else {
                System.err.println("Grid image not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.saveDateLabel = new JLabel();
    }
    public void setSaveDate(Date saveDate) {
        saveDateLabel.setText("Save Date: " + saveDate.toString());
    }
    /**
     * Sets up the main components for the play mode, including the game grid, control panel, health display, and inventory panel.
     * Adds event listeners for user input and prepares the game view for rendering.
     */

    @Override
    public void initialize() {
        playPanel = new JPanel(new BorderLayout());
        initializeUI();

        initializeHealthHeartDisplay();

        initializeControlPanel();

        inventoryPanel = new InventoryPanel(player);
        inventoryPanel.setPreferredSize(new Dimension(150, inventoryPanel.getPreferredSize().height)); // Adjust width to 150


        // Add the leftTopPanel to the top-left of the playPanel
        playPanel.add(inventoryPanel, BorderLayout.WEST);
        playPanel.addKeyListener(inputHandler);
        playPanel.setFocusable(true);
        playPanel.requestFocusInWindow();
    }

    private void initializeHealthHeartDisplay() {
        healthHeartDisplay = new HealthHeartDisplay(player, controller);
        if (player != null) {
            player.addObserver(healthHeartDisplay);
        }

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(healthHeartDisplay.getPanel(), BorderLayout.CENTER);
        playPanel.add(topPanel, BorderLayout.NORTH);
    }

    private void initializeUI() {
        JPanel gridPanelWithPadding = new JPanel(new BorderLayout());
        gridPanelWithPadding.add(createPaddingPanel(0, 50), BorderLayout.NORTH);
        gridPanelWithPadding.add(createPaddingPanel(0, 50), BorderLayout.SOUTH);
        gridPanelWithPadding.add(createPaddingPanel(50, 0), BorderLayout.WEST);
        gridPanelWithPadding.add(createPaddingPanel(50, 0), BorderLayout.EAST);
        playPanel.add(new TimerDisplay(30, controller.getGameState()).getPanel(), BorderLayout.SOUTH);


        JPanel gridPanel = new JPanel(new GridLayout(16, 16));
        gridPanels = new JPanel[16][16];

        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 16; col++) {
                int finalCol = col;
                int finalRow = row;

                JPanel panel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);

                        Map<Point, GameObject> gameObjects = controller.getGameState().getHall().getGameObjects();
                        Point point = new Point(finalCol, finalRow);

//                        // Log all game objects in the current hall
//                        gameObjects.forEach((pos, obj) -> {
//                            System.out.println("Object at " + pos + ": " + obj.getClass().getSimpleName());
//                        });

                        if (gridImage != null) {
                            Image scaledImage = gridImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                            g.drawImage(scaledImage, 0, 0, this);
                        }
                        GameObject playerObject = controller.getPlayerObject();
                        if (playerObject != null) {
                            Point playerPosition = playerObject.getPosition();

                            if (finalRow == playerPosition.y && finalCol == playerPosition.x) {
                                GameEntityImage renderer = controller.getGameEntityRenderer(playerObject);
                                if (renderer != null) {
                                    renderer.render(g, playerObject, 0, 0);
                                } else {
                                    System.err.println("No renderer found for player object.");
                                }
                            } else {
                                if (gameObjects.containsKey(point)) {
                                    GameObject obj = gameObjects.get(point);
                                    if (obj != null) {
                                        GameEntityImage renderer = controller.getGameEntityRenderer(obj);
                                        if (renderer != null) {
                                            renderer.render(g, obj, 0, 0);
                                        } else {
                                            System.err.println("No renderer found for: " + obj.getClass().getSimpleName());
                                        }
                                    }
                                }
                            }
                        }
                    }

                };

                panel.setPreferredSize(new Dimension(40, 40));


                panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        inputHandler.mouseClicked(e);
                    }
                });

                gridPanels[row][col] = panel;
                gridPanel.add(panel);
            }
        }

        gridPanelWithPadding.add(gridPanel, BorderLayout.CENTER);
        playPanel.add(gridPanelWithPadding, BorderLayout.CENTER);
    }

    public JPanel[][] getGridPanels() {
        return gridPanels;
    }    

    private JPanel createPaddingPanel(int width, int height) {
        JPanel paddingPanel = new JPanel();
        paddingPanel.setPreferredSize(new Dimension(width, height));
        return paddingPanel;
    }

    private int showConfirmDialog() {
        return JOptionPane.showConfirmDialog(
                controlPanel,
                "Are you sure you want to move forward to the game? You won't be able to make any further changes if you proceed.",
                "Confirm Submission",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void initializeControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        pauseButton = new JButton("Pause");
        resumeButton = new JButton("Resume");
        quitButton = new JButton("Quit Trial");
        saveGameButton = new JButton("Save Game");

        // Add listeners for pause and resume
        pauseButton.addActionListener(e -> controller.pauseGame());
        resumeButton.addActionListener(e -> {
            controller.resumeGame();
            playPanel.requestFocusInWindow();
            playPanel.revalidate();
            playPanel.repaint();
        });

        quitButton.addActionListener(e -> {
            int confirmation = showConfirmDialog();
            if (confirmation == JOptionPane.YES_OPTION) {
                controller.stopGame();
                controller.notifyGameOver();
            }
        });

        controlPanel.add(saveDateLabel);
        controlPanel.add(pauseButton);
        controlPanel.add(resumeButton);
        controlPanel.add(quitButton);
        controlPanel.add(saveGameButton);

        playPanel.add(controlPanel, BorderLayout.SOUTH);
    }

    public JButton getSaveGameButton() {
        return saveGameButton;
    }
    @Override
    public void isPlayerDead() {
        controller.pauseGame();
        BufferedImage snapshot = captureSnapshot();
        controller.onGameOver(snapshot);
    }


    @Override
    public void render() {
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 16; col++) {
                gridPanels[row][col].repaint();
            }
        }

        if (inventoryPanel != null) {
            inventoryPanel.repaint();
        }
        if (highlightRectangle != null) {
            SwingUtilities.invokeLater(() -> {
                Graphics g = playPanel.getGraphics();
                if (g != null) {
                    try {
                        g.setColor(new Color(255, 255, 255, 128));

                        int offsetLeft = 280;
                        int offsetTop = -180;

                        int px = offsetLeft + highlightRectangle.x * 60;
                        int py = offsetTop + highlightRectangle.y * 60;
                        int pw = highlightRectangle.width * 60;
                        int ph = highlightRectangle.height * 60;

                        g.fillRect(px, py, pw, ph);

                    } finally {
                        g.dispose();
                    }
                }
            });
        }
        //Since grid is rendered here we can also render the arrows here for the future implementations
//        for (GameObject obj : controller.getGameState().getHall().getGameObjects().values()) {
//            if (obj instanceof Arrow) {
//                Arrow arrow = (Arrow) obj;
//                arrow.moveArrow();
//            }
//        }
    }
    public void setHighlightRectangle(Rectangle rect) {
        this.highlightRectangle = rect;
    }

    public BufferedImage captureSnapshot() {
        BufferedImage snapshot = new BufferedImage(playPanel.getWidth(), playPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = snapshot.createGraphics();
        playPanel.paint(g2d);
        g2d.dispose();

        return snapshot;
    }

    @Override
    public void teardown() {
        if (playPanel != null) {
            playPanel.removeAll();
            playPanel.revalidate();
            playPanel.repaint();
        }
    }

    @Override
    public JPanel getPanel() {
        return playPanel;
    }

    @Override
    public void onObjectAdded() {
        render();
    }

    @Override
    public void onObjectReplaced(){
        render();
    }

    public InventoryPanel getInventoryPanel() {
        return inventoryPanel;
    }

    public void highlightArea(Rectangle area) {
        this.highlightRectangle = area;
        render();
    }

    public void clearHighlight() {
        this.highlightRectangle = null;
        render();
    }
}
