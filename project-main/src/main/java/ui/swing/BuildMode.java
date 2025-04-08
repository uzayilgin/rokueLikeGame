/**
 * BuildMode provides a user interface for designing and placing objects in a grid-based hall structure.
 * It supports object selection, placement, deletion, and validation of the designed hall before submission.
 * Implements the GameView interface for easy integration with the game's UI system.
 */

package ui.swing;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Objects;

import domain.behaviors.HallStrategy;
import domain.gameObjects.*;
import technicalServices.logging.LogManager;
import technicalServices.placement.ObjectPlacementManager;
import ui.gameObjectImage.BlockImage;
import ui.gameObjectImage.ChestImage;
import ui.gameObjectImage.WallDifferentImage;
import ui.gameObjectImage.WallImage;
import domain.utilities.Constants;

public class BuildMode implements GameView {
    private JPanel buildPanel;
    private Object selectedObject = null;
    private JPanel[][] gridPanels;
    private String[][] placedObjects;
    private JPanel objectPanel;
    private boolean isPlacementPhase = true;
    private int heroRow = 0;
    private int heroCol = 0;
    private HallStrategy hallStrategy;
    private JButton deleteButton;
    private JButton submitButton;
    private ArrayList<JButton> objectButtons = new ArrayList<>();
    private ArrayList<Point> placedObjectLocations = new ArrayList<>();
    private Constants.HallType currentHall = Constants.HallType.EARTH;
    private final EnumMap<Constants.HallType, Integer> hallObjectRequirements;
    private ObjectPlacementManager placementManager;
    /**
     * Constructor to initialize the BuildMode with a specific hall strategy.
     * Sets up the grid, object placement manager, and UI components like object buttons and grid cells.
     * @param hallStrategy Defines the requirements and behavior of the hall being designed.
     */

    public BuildMode(HallStrategy hallStrategy) {
        this.hallStrategy = hallStrategy;
        buildPanel = new JPanel();
        hallObjectRequirements = new EnumMap<>(Constants.HallType.class);
        hallObjectRequirements.put(Constants.HallType.EARTH, 6);  // Earth requires at least 6 objects
        hallObjectRequirements.put(Constants.HallType.FIRE, 17); 
        hallObjectRequirements.put(Constants.HallType.WATER, 13);
        hallObjectRequirements.put(Constants.HallType.AIR, 9);

        placementManager = new ObjectPlacementManager(hallStrategy.getRequirements());

        buildPanel.setBackground(new Color(0,0,0,0));
        ImageIcon backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/images/Dark_lvl3(2).jpg")));
        Image img = backgroundImage.getImage();

        buildPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int x = 0; x < getWidth(); x += img.getWidth(null)) {
                    for (int y = 0; y < getHeight(); y += img.getHeight(null)) {
                        g.drawImage(img, x, y, this);
                    }
                }
            }
        };

        buildPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcBuild = new GridBagConstraints();

        JPanel gridPanelWithPadding = new JPanel(); // wrapper panel for grid panel that also displays border
        gridPanelWithPadding.setLayout(new GridBagLayout());
        gbcBuild.gridx = 0;
        buildPanel.add(gridPanelWithPadding, gbcBuild);
        GridBagConstraints gbcPad = new GridBagConstraints();
        gbcPad.insets = new Insets(0, 0, 0, 0);
        gridPanelWithPadding.setBorder(new ImageBorder("/assets/images/borderOrange.png"));
        gridPanelWithPadding.setBackground(new Color(0,0,0,0));
        gridPanelWithPadding.setSize(new Dimension(1120, 1120));

        JPanel gridPanel = new JPanel(new GridLayout(16, 16));
        gbcPad.gridx = 1;
        gbcPad.gridy = 1;
        gridPanelWithPadding.add(gridPanel, gbcPad);
        gridPanel.setBackground(new Color(0,0,0,0));

        gridPanels = new JPanel[16][16];
        placedObjects = new String[16][16];

        ImageIcon tileImage = new ImageIcon(getClass().getResource("/assets/tiles/tile.png"));
        Image resizedImage = tileImage.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 16; col++) {
                JPanel cellPanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.drawImage(resizedImage, 0, 0, this);
                    }
                };
                cellPanel.setPreferredSize(new Dimension(40, 40));
                cellPanel.setBackground(new Color(0,0,0,0));
                final int r = row;
                final int c = col;

                cellPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (isPlacementPhase && selectedObject != null) {
                            placeObject(r, c);
                        }
                    }
                });

                gridPanels[row][col] = cellPanel;
                gridPanel.add(cellPanel);
                gridPanel.setBackground(new Color(0,0,0,0));
            }
        }

        // puts distance between grid and object panel
        JPanel buildPadding = new JPanel();
        buildPadding.setPreferredSize(new Dimension(20, 320));
        buildPadding.setBackground(new Color(0,0,0,0));
        gbcBuild.gridx = 1;
        buildPanel.add(buildPadding, gbcBuild);

        JPanel objectBorderPanel = new JPanel(); // wrapper panel for object panel that displays border
        objectBorderPanel.setLayout(new GridBagLayout());
        objectBorderPanel.setPreferredSize(new Dimension(250, 330));
        objectBorderPanel.setBorder(new ImageBorder("/assets/images/borderOrange.png"));
        objectBorderPanel.setBackground(new Color(0,0,0,0));
        GridBagConstraints objectBorderPanelGbc = new GridBagConstraints();
        objectBorderPanelGbc.anchor = GridBagConstraints.CENTER;
        objectBorderPanelGbc.fill = GridBagConstraints.HORIZONTAL;
        objectBorderPanelGbc.insets = new Insets(0, 0, 0, 0);
        objectBorderPanelGbc.gridx = 0;
        objectBorderPanelGbc.gridy = 0;

        buildPanel.add(objectBorderPanel, objectBorderPanelGbc);

        gbcBuild.gridx = 2;
        buildPanel.add(objectBorderPanel, gbcBuild);
        objectPanel = new JPanel();
        objectPanel.setLayout(new GridBagLayout());
        objectPanel.setPreferredSize(new Dimension(240, 320));
        objectBorderPanel.add(objectPanel, objectBorderPanelGbc);


        // setting the basics of the gridbag layout for object panel design
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 8, 13, 8);

        // title for objects panel
        ImageIcon icon = new ImageIcon(getClass().getResource("/assets/images/OBJECTS.png"));
        Image img1 = icon.getImage();
        Image resizedImageNew = img1.getScaledInstance(225, 65, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImageNew);
        JLabel label = new JLabel(resizedIcon);
        gbc.gridx = 0;
        gbc.gridy = 0;
        objectPanel.add(label);
        objectPanel.setBackground(Color.decode("#a5b7be"));

        JPanel paddingPanel = new JPanel();
        paddingPanel.setPreferredSize(new Dimension(250, 10));
        paddingPanel.setBackground(new Color(0,0,0,0));
        gbc.gridy = 1;
        objectPanel.add(paddingPanel, gbc);

        Object Chest = new Chest(0,0);
        addObjectButton("Chest", Chest, gbc, 5, 0);
        gbc.gridy++;
        Object Wall = new Wall(0,0);
        addObjectButton("Wall", Wall, gbc, 0, 0);
        gbc.gridy++;
        Object Block = new Block(0,0);
        addObjectButton("Block", Block, gbc, 0, 0);
        gbc.gridy++;
        Object WallDifferent = new WallDifferent(0,0);
        addObjectButton("Diff Wall", WallDifferent, gbc, 0, 20);
        gbc.gridy++;

        deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(0x7A5937));
        deleteButton.setForeground(Color.decode("#050813"));
        deleteButton.setFont(new Font("Garamond", Font.BOLD, 20));
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        objectPanel.add(deleteButton, gbc);

        submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(0x7A5937));
        submitButton.setForeground(Color.decode("#050813"));
        submitButton.setFont(new Font("Garamond", Font.BOLD, 20));
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        objectPanel.add(submitButton, gbc);

        buildPanel.setFocusable(true);
        buildPanel.requestFocusInWindow();
    }
    /**
     * Validates whether the placed objects meet the minimum required count for the current hall type.
     * Displays a warning if the requirements are not met.
     * @return true if the hall is valid, false otherwise.
     */

    public boolean validateHall() {
        int requiredObjects = hallStrategy.getRequirements();
        int placedObjectCount = placementManager.getPlacedObjectCount();
        currentHall = hallStrategy.getHallType();

        if (placedObjectCount < requiredObjects) {
            JOptionPane.showMessageDialog(buildPanel,
                    "You have placed " + placedObjectCount + " objects in " + currentHall +
                            " Hall. At least " + requiredObjects + " are required.");
            return false;
        }

        return true;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getSubmitButton() {
        return submitButton;
    }

    public ArrayList<JButton> getObjectButtons() {
        return objectButtons;
    }

    public void selectObject(Object object) {
        selectedObject = object;
        System.out.println("Object selected: " + selectedObject);
    }
    /**
     * Adds a button to the object panel for selecting and placing objects in the grid.
     * Configures the button with an icon, action listener, and custom styling.
     *
     * @param objectName The name of the object to be displayed on the button.
     * @param object The object instance associated with the button for placement.
     * @param gbc The GridBagConstraints used for placing the button in the layout.
     * @param verticalSpacingBefore The top margin before the button.
     * @param verticalSpacingAfter The bottom margin after the button.
     */

    private void addObjectButton(String objectName, Object object, GridBagConstraints gbc, int verticalSpacingBefore, int verticalSpacingAfter) {
        try {
            ImageIcon icon = getIconForObject(object);
            if (icon == null) {
                System.err.println("Icon not found for object: " + objectName);
                return;
            }
            JButton objectButton = new JButton(objectName, icon);
            objectButton.setBackground(new Color(0x7A5937));
            objectButton.setForeground(Color.decode("#050813"));
            objectButton.setFont(new Font("Garamond", Font.BOLD, 20));
            objectButton.addActionListener(e -> selectObject(object));
            objectButtons.add(objectButton);
            objectPanel.add(Box.createVerticalStrut(10));
            gbc.insets = new Insets(verticalSpacingBefore, 0, verticalSpacingAfter, 0);
            objectPanel.add(objectButton, gbc);
        } catch (Exception e) {
            e.printStackTrace();
            LogManager.logError(e.getMessage());
        }
    }


    private ImageIcon getIconForObject(Object object) {
        ImageIcon icon = null;
        if (object instanceof Chest) {
            icon = new ImageIcon(ChestImage.getImageForChest((Chest) object));
        } else if (object instanceof Wall) {
            icon = new ImageIcon(WallImage.getImageForWall((Wall) object));
        } else if (object instanceof Block) {
            icon = new ImageIcon(BlockImage.getImageForBlock((Block) object));
        } else if (object instanceof WallDifferent) {
            icon = new ImageIcon(WallDifferentImage.getImageForWallDifferent((WallDifferent) object));
        }
        //System.out.println("Fetched icon for " + object + ": " + (icon != null ? "Success" : "Failed"));
        return icon;
    }


    public int showConfirmDialog() {
        int confirmation = JOptionPane.showConfirmDialog(
                buildPanel,
                "Are you sure you want to move forward to the game? You won't be able to make any further changes if you proceed.",
                "Confirm Submission",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        return confirmation;
    }

    public void setPlacementPhase(boolean value) {
        this.isPlacementPhase = value;
    }

    public boolean isPlacementPhase() {
        return isPlacementPhase;
    }
    /**
     * Handles the placement or deletion of objects in a specific grid cell.
     * Checks for placement rules, updates the UI, and manages the placement manager state.
     * @param row The row index of the grid cell.
     * @param col The column index of the grid cell.
     */

    private void placeObject(int row, int col) {
        System.out.println("Attempting to place object at " + row + "," + col + " with selectedObject=" + selectedObject);
        if (selectedObject != null) {
            Point point = new Point(row, col);

            if ("Delete".equals(selectedObject)) {
                // Remove object
                if (placementManager.removeObject(point)) {
                    gridPanels[row][col].removeAll();
                    placedObjects[row][col] = null;
                    gridPanels[row][col].revalidate();
                    gridPanels[row][col].repaint();
                    System.out.println("Deleted object at " + row + "," + col);
                } else {
                    JOptionPane.showMessageDialog(null, "No object to delete here!");
                }
            } else if (placedObjects[row][col] == null) {
                // Place object
                if (placementManager.placeObject(point, selectedObject.toString())) {
                    ImageIcon icon = getIconForObject(selectedObject);

                    if (icon != null) {
                        // for resizing the icon so that it fits the cells
                        Image img = icon.getImage();
                        int cellWidth = gridPanels[row][col].getWidth();
                        int cellHeight = gridPanels[row][col].getHeight();
                        Image resizedImage = img.getScaledInstance(cellWidth - 7, cellHeight - 7, Image.SCALE_SMOOTH);
                        icon = new ImageIcon(resizedImage);
                    }

                    JLabel objectLabel;
                    if (icon != null) {
                        objectLabel = new JLabel(icon);
                    } else {
                        objectLabel = new JLabel(selectedObject.toString());
                    }

                    gridPanels[row][col].removeAll();
                    gridPanels[row][col].add(objectLabel);
                    gridPanels[row][col].revalidate();
                    gridPanels[row][col].repaint();

                    placedObjects[row][col] = selectedObject.toString();
                    System.out.println("Placed " + selectedObject + " at " + row + "," + col);
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot place object here!");
                }
            } else {
                if (selectedObject instanceof Rune){
                    System.out.println("Placing Rune at " + row + "," + col);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Cell already occupied by: " + placedObjects[row][col]);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No object selected!");
        }
    }
    /**
     * Prepares the BuildMode for a new design session.
     * Resets all grid cells, clears placed objects, and reinitializes the placement manager.
     */

    @Override
    public void initialize() {
        isPlacementPhase = true;
        heroRow = 0;
        heroCol = 0;
        selectedObject = null;
        placedObjectLocations.clear();
        placementManager = new ObjectPlacementManager(hallStrategy.getRequirements());

        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 16; col++) {
                gridPanels[row][col].removeAll();
                gridPanels[row][col].revalidate();
                gridPanels[row][col].repaint();
                placedObjects[row][col] = null;
            }
        }
        buildPanel.requestFocusInWindow();
    }

    @Override
    public void render() {
        buildPanel.revalidate();
        buildPanel.repaint();
    }

    @Override
    public void teardown() {
        selectedObject = null;
        placedObjectLocations.clear();
        buildPanel.removeAll();
        buildPanel.revalidate();
        buildPanel.repaint();
    }

    @Override
    public JPanel getPanel() {
        return buildPanel;
    }

    public String[][] getPlacedObjects() {
        return placedObjects;
    }

}
