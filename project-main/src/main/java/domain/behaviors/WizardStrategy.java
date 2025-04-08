/**
 * The WizardStrategy defines an interface for behaviors
 * associated with a WizardMonster.
 *
 * Implementing classes should provide specific logic for executing
 * a strategy within a given Hall and an optional stop behavior.
 */
package domain.behaviors;
import domain.gameObjects.Hall;
import domain.gameObjects.WizardMonster;

public interface WizardStrategy {
    void execute(WizardMonster monster, Hall hall);
    default void stop(){ }
}
