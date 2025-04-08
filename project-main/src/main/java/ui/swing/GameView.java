/**
 * Represents a generic view in the game UI.
 * Provides methods for initialization, rendering, cleanup, and retrieving the main panel.
 */

package ui.swing;

import javax.swing.*;

public interface GameView {
    void initialize(); // Set up UI elements and listeners
    void render();     // Render the view (called by the GameController)
    void teardown();   // Clean up before switching to another view
    JPanel getPanel(); // Return the main panel
}
