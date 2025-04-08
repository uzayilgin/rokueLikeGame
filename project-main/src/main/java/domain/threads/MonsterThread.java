/**
 * MonsterThread represents a threaded behavior for managing a specific monster's actions in the game.
 * Each monster type has its own specific implementation of this class.
 * The thread manages the monster's actions, including movement, interaction, and rendering, while respecting
 * the game's paused and game-over states.
 */

package domain.threads;

import domain.gameCore.GameState;
import domain.gameObjects.Monster;
import technicalServices.logging.LogManager;

import java.awt.*;

public abstract class MonsterThread implements Runnable {
    protected int x, y;
    protected boolean alive = true;
    protected GameState model;
    protected Monster monster;

    public MonsterThread(Monster monster, GameState model) {
        this.monster = monster;
        this.model = model;
    }

    public abstract void draw(Graphics g);

    public abstract void act();

    public int getX() {
        return monster.getPosition().x;
    }

    public int getY() {
        return monster.getPosition().y;
    }

    public void kill() {
        alive = false;
    }

    public Monster getMonster() {
        return monster;
    }
    /**
     * The main execution loop for the MonsterThread.
     * Continuously manages the monster's behavior, handling pauses and ensuring
     * proper termination when the game ends or the thread is explicitly killed.
     */

    @Override
    public void run() {
        while (alive) {

            while (model.isPaused()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            LogManager.logInfo("Monster thread resumed. [from class: MonsterThread, method: run]");
            System.out.println("Monster thread resumed");

            //model.repaint();

            try {
                act();
                Thread.sleep(1);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (model.isGameOver()) {
                break;
            }
        }
    }
}
