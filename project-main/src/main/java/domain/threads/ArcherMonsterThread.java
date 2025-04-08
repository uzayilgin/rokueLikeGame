/**
 * Thread implementation for controlling the behavior of an `ArcherMonster` in the game.
 * Handles movement, interactions, and actions of the `ArcherMonster` on a separate thread.
 */
package domain.threads;


import domain.gameCore.GameState;
import domain.gameObjects.ArcherMonster;
import java.awt.*;


public class ArcherMonsterThread extends MonsterThread {
    public ArcherMonsterThread(ArcherMonster monster, GameState model) {
        super(monster, model);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x * 50, y * 50, 50, 50);
    }

    /**
     * Defines the behavior of the `ArcherMonster` during its turn.
     * Checks the distance to the player and attacks if within range.
     * Updates the game state and notifies listeners of changes.
     */
    @Override
    public void act() {
        //instead of those we could do this:, and monsterthread could be enough on its own in that case
        // this.monster.attack(model.getPlayer());
        int heroX = model.getPlayer().getPosition().x;
        int heroY = model.getPlayer().getPosition().y;

        // Check distance to hero
        int distance = Math.abs(heroX - monster.getPosition().x) + Math.abs(heroY - monster.getPosition().y);
        if (distance <= 4) {
            model.getPlayer().setLifeCount(model.getPlayer().getLifeCount() - monster.getAttackDamage());

        }
        model.getHall().notifyListeners();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

