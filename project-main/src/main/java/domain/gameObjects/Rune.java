/**
 * Represents a Rune object in the game.
 * Runes are associated with a specific hall type and can be revealed during gameplay.
 */
package domain.gameObjects;

import domain.utilities.Constants;
import technicalServices.logging.LogManager;

public class Rune extends GameObject {
    private boolean revealed;
    private Constants.HallType runeType;

    public Rune(int x, int y, Constants.HallType hallType) {
        super(x, y);
        this.revealed = false;
        this.runeType = hallType;
    }

    public Constants.HallType getRuneType() {
        return runeType;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }
    /**
     * Reveals the rune if it is not already revealed.
     */
    public void reveal() {
        if (!revealed) {
            revealed = true;
            LogManager.logInfo("Rune has been revealed! [Rune.reveal()]");
            System.out.println("Rune has been revealed!");
        } else {
            LogManager.logInfo("Rune is already revealed. [Rune.reveal()]");
            System.out.println("Rune is already revealed.");
        }
    }
    /**
     * Interacts with the rune, revealing it if it is not already revealed.
     */
    @Override
    public void interact() {
        if (!revealed) {
            reveal();
        } else {
            LogManager.logInfo("Rune is already revealed. [Rune.interact()]");
            System.out.println("Rune is already revealed.");
        }
    }

}
