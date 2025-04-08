/**
 * Handles player inputs such as keyboard and mouse interactions during gameplay.
 * Integrates with `GameController`, `PlayerController`, and `PlayMode` to manage movement, interactions, and enchantment use.
 */

package technicalServices.input;

import domain.controllers.GameController;
import domain.controllers.PlayerController;
import domain.gameCore.GameState;
import domain.gameObjects.*;
import ui.swing.PlayMode;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import assets.audio.SoundProcessor;

public class InputHandler implements KeyListener, MouseListener{
    private final GameController gameController;
    private final PlayerController playerController;
    private PlayMode playMode;
    /**
     * Constructs an InputHandler to process keyboard and mouse events.
     *
     * @param gameController   The game controller for overall game state management.
     * @param playerController The player controller for managing player actions.
     * @param playMode         The PlayMode UI component for rendering and interaction.
     */

    public InputHandler(GameController gameController, PlayerController playerController, PlayMode playMode) {
        this.playMode = playMode;
        this.gameController = gameController;
        this.playerController = playerController;
    }
    /**
     * Handles key press events for player movement and enchantment usage.
     *
     * @param e The KeyEvent representing the pressed key.
     */

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameController.isGamePaused()) {
            return;
        }else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    playerController.movePlayerLeft();
                    playMode.render();
                    break;
                case KeyEvent.VK_UP:
                    playerController.movePlayerUp();
                    playMode.render();
                    break;
                case KeyEvent.VK_DOWN:
                    playerController.movePlayerDown();
                    playMode.render();
                    break;
                case KeyEvent.VK_RIGHT:
                    playerController.movePlayerRight();
                    playMode.render();
                    break;
                case KeyEvent.VK_B:
                    handleLuringGem();
                    playMode.render();
                    break;
                case KeyEvent.VK_C:
                    handleCloakEnchantment();
                    playMode.render();
                    break;
                case KeyEvent.VK_R:
                    handleRevealEnchantment();
                    break;
                }
            }
            System.out.println("Player Position: " + gameController.getPlayerObject().getPosition());
        }
    /**
     * Activates a Reveal Enchantment, highlighting the area around the Rune in the hall.
     * Checks the player's inventory for the enchantment and manages the highlight effect.
     */

    private void handleRevealEnchantment() {
        Player player = gameController.getPlayerObject();
        Hall hall = gameController.getGameState().getHall();

        boolean hasRevealEnchantment = hall.getActiveEnchantments().stream()
                .anyMatch(enchantment -> enchantment instanceof RevealEnchantment);

        if (!hasRevealEnchantment) {
            System.out.println("No Reveal Enchantment in inventory.");
            return;
        }
        playerController.removeReveal();
        Point runePosition = hall.getRuneObjects().keySet().stream().findFirst().orElse(null);
        if (runePosition == null) {
            System.out.println("No rune found in the hall.");
            return;
        }
        int x = runePosition.x;
        int y = runePosition.y;
        int w = Math.min(4, 16-x);
        int h = Math.min(4, 16-y);
        System.out.println("Rune Position: " + runePosition);

        playMode.setHighlightRectangle(new Rectangle(x,y,w,h));

        Timer timer = new Timer(10_000, ev -> {
            playMode.clearHighlight();
        });
        timer.setRepeats(false);
        timer.start();
    }
    /**
     * Handles key release events to stop player movement.
     *
     * @param e The KeyEvent representing the released key.
     */

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_RIGHT:
                playerController.stopPlayerMoving();
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Unused but required by KeyListener
    }
    /**
     * Handles mouse click events for interacting with objects in the game grid.
     * Checks if the clicked panel corresponds to an adjacent object and processes the interaction.
     *
     * @param e The MouseEvent representing the click.
     */

    @Override
    public void mouseClicked(MouseEvent e) {
        Component source = e.getComponent();
        if (source instanceof JPanel) {
            SoundProcessor processSound = new SoundProcessor("src/main/java/assets/audio/undertale-select.wav");
            JPanel clickedPanel = (JPanel) source; // finds the panel that is interacted with
            //iterates through game grid to check if the clicked panel is in the grid 
            for (int row = 0; row < playMode.getGridPanels().length; row++) {
                for (int col = 0; col < playMode.getGridPanels()[row].length; col++) {
                    if (playMode.getGridPanels()[row][col] == clickedPanel) {
                        // found clicked panel -> create the point for interaction
                        Point clickedPoint = new Point(col, row);
                        // checks if clicked object is adjacent to player and interacts if true 
                        if (playerController.playerInteract(clickedPoint)) {
                            System.out.println("Player interacted with object at: " + clickedPoint);
                            // play sound if true 
                            processSound.playSound();
                        }
                        return;
                    }
                }
            }
        }
    }
    

    @Override
    public void mousePressed(MouseEvent e) {
        // not used 
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // not used
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // not used 
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // not used
    }
    /**
     * Handles the use of a Luring Gem Enchantment.
     * Checks the player's inventory for a Luring Gem, allows directional placement, and activates the lure.
     */

    private void handleLuringGem() {
        GameState gameState = gameController.getGameState(); // Access GameState
        Player player = gameState.getPlayer();
        Hall hall = gameState.getHall();

        // Check if a Luring Gem is in the active enchantments
        boolean hasLuringGem = hall.getActiveEnchantments().stream()
                .anyMatch(enchantment -> enchantment instanceof LuringGemEnchantment);

        int luringGemCount = 0;
        LuringGemEnchantment lastLuringGem = null;
        for (Enchantment enchantment : gameState.getPlayer().getEnchantments().keySet()) {
            if (enchantment instanceof LuringGemEnchantment) {
                lastLuringGem = (LuringGemEnchantment) enchantment;
                luringGemCount++;
            }
        }


        if (lastLuringGem != null) {
            System.out.println("Luring Gem is in inventory");
            System.out.println("Press A, D, W, or S to choose the direction to throw the Luring Gem.");

            LuringGemEnchantment finalLastLuringGem = lastLuringGem;
            playMode.addTemporaryKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    int direction = e.getKeyCode();
                    if (direction == KeyEvent.VK_A || direction == KeyEvent.VK_D ||
                            direction == KeyEvent.VK_W || direction == KeyEvent.VK_S) {
                        throwLuringGem(direction, gameState);
                        finalLastLuringGem.setIsActive(1, gameState.getHall().getLuringGemPosition());
                        playMode.removeTemporaryKeyListener(this); // Remove this listener
                    }
                }
            });
        } else {
            System.out.println("No Luring Gem in inventory.");
        }
    }
    /**
     * Throws a Luring Gem in the specified direction.
     *
     * @param direction  The direction to throw the Luring Gem (based on key press).
     * @param gameState  The current game state.
     */

    private void throwLuringGem(int direction, GameState gameState) {
        Player player = gameState.getPlayer();
        Hall hall = gameState.getHall();
        Point playerPosition = player.getPosition();
        Point lurePosition = calculateLurePosition(playerPosition, direction);

        if (lurePosition != null && hall.getEmptyPositions().contains(lurePosition)) {
            // Create a Luring Gem object at the lure position
            LuringGemEnchantment lure = new LuringGemEnchantment(lurePosition.x, lurePosition.y);
            hall.addObject(lure); // Add the lure to the hall

            // Remove the Luring Gem from active enchantments
            hall.getActiveEnchantments().removeIf(enchantment -> enchantment instanceof LuringGemEnchantment);

            System.out.println("Luring Gem thrown at " + lurePosition);
        } else {
            System.out.println("Invalid position for Lure.");
        }
    }
    /**
     * Calculates the target position for placing a Luring Gem based on the player's position and direction.
     *
     * @param playerPosition The player's current position.
     * @param direction      The direction for the Luring Gem placement.
     * @return The calculated position or null if the direction is invalid.
     */

    private Point calculateLurePosition(Point playerPosition, int direction) {
        switch (direction) {
            case KeyEvent.VK_A: return new Point(playerPosition.x - 1, playerPosition.y); // Left
            case KeyEvent.VK_D: return new Point(playerPosition.x + 1, playerPosition.y); // Right
            case KeyEvent.VK_W: return new Point(playerPosition.x, playerPosition.y - 1); // Up
            case KeyEvent.VK_S: return new Point(playerPosition.x, playerPosition.y + 1); // Down
            default: return null;
        }
    }
    /**
     * Activates the Cloak Enchantment if available in the player's inventory.
     * Removes the cloak from the inventory and applies its effect.
     *
     * @return True if the enchantment was successfully used, false otherwise.
     */

    private boolean handleCloakEnchantment() {
        if (playerController.hasCloak()) {
            try {
                playerController.removeCloak();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            playerController.useCloak();
            return true;
        } else {
            return false;
        }
    }



}
