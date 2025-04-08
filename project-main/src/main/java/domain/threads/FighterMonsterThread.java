/**
 * FighterMonsterThread manages the behavior and movement of a FighterMonster.
 * This thread executes the monster's logic, including attacking the player and reacting to luring gems.
 */

package domain.threads;

import domain.gameCore.GameState;
import domain.gameObjects.FighterMonster;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FighterMonsterThread extends MonsterThread {
    private final Random random = new Random();
    private List<Point> possibleMoves;
    public FighterMonsterThread(FighterMonster monster, GameState model) {
        super(monster, model);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(monster.getPosition().x * 50, monster.getPosition().y * 50, 50, 50);
    }

    private Point getNextPositionTowards(Point target, Point current) {
        int dx = Integer.compare(target.x, current.x);
        int dy = Integer.compare(target.y, current.y);

        Point[] possibleMoves = {
                new Point(current.x + dx, current.y),
                new Point(current.x, current.y + dy)
        };

        for (Point move : possibleMoves) {
            if (model.getHall().getEmptyPositions().contains(move)) {
                return move;
            }
        }
        return null;
    }

    /**
     * Determines a random valid position that brings the monster closer to the target (e.g., a luring gem).
     *
     * @param target  The target position to move towards.
     * @param current The monster's current position.
     * @return A valid position closer to the target, or null if no valid move exists.
     */

    private Point getRandomPointTowardsGem(Point target, Point current) {
        int currentDistance = Math.abs(current.x - target.x) + Math.abs(current.y - target.y);

        List<Point> possibleMoves = List.of(
                new Point(current.x + 1, current.y),  // Right
                new Point(current.x - 1, current.y),  // Left
                new Point(current.x, current.y + 1),  // Down
                new Point(current.x, current.y - 1)   // Up
        );

        List<Point> validMoves = possibleMoves.stream()
                .filter(move -> model.getHall().getEmptyPositions().contains(move))
                .filter(move -> {
                    int newDistance = Math.abs(move.x - target.x) + Math.abs(move.y - target.y);
                    return newDistance < currentDistance;
                })
                .toList();

        System.out.println("Monster at " + current + " moving towards " + target + ". Valid moves: " + validMoves);

        if (validMoves.isEmpty()) {
            return null;
        }

        return validMoves.get(random.nextInt(validMoves.size()));
    }

    /**
     * Determines a random valid position for the monster to move to.
     *
     * @return A valid position within the hall, or null if no valid move exists.
     */

    private Point getRandomMove() {
        List<Point> possibleMoves = List.of(
                new Point(Math.max(monster.getPosition().x - 1, 0), monster.getPosition().y), // Left
                new Point(Math.min(monster.getPosition().x + 1, 16), monster.getPosition().y), // Right
                new Point(monster.getPosition().x, Math.max(monster.getPosition().y - 1, 0)), // Up
                new Point(monster.getPosition().x, Math.min(monster.getPosition().y + 1, 16))  // Down
        );

        possibleMoves = possibleMoves.stream()
                .filter(move -> model.getHall().getEmptyPositions().contains(move))
                .toList();

        if (possibleMoves.isEmpty()) {
            return null;
        }

        return possibleMoves.get(random.nextInt(possibleMoves.size()));
    }
    /**
     * The main behavior logic for the FighterMonster.
     * Handles movement, attacking the player, and reacting to luring gems.
     * Includes safeguards against race conditions and game pauses.
     */

    @Override
    public void act() {
        while (true) {
            while (model.isPaused()) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }


            try {

                if (Math.abs(model.getPlayer().getPosition().x - monster.getPosition().x) + Math.abs(model.getPlayer().getPosition().y - monster.getPosition().y) == 1) {
                    model.getPlayer().setLifeCount(model.getPlayer().getLifeCount() - 1); // for this implement a function.
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }



                Point oldPoint = monster.getPosition();
                Point newPoint;

                Point luringGemPosition = model.getHall().getLuringGemPosition();
                if (luringGemPosition != null) {
                    newPoint = getRandomPointTowardsGem(luringGemPosition, oldPoint);
                } else {
                    newPoint = getRandomMove();
                }

                if (newPoint != null) {
                    model.getHall().moveObject(oldPoint, newPoint);

                    monster.move(new ArrayList<>() {{
                        add(newPoint);
                    }});

                    model.getHall().notifyListeners();
                    System.out.println("Fighter moved from " + oldPoint + " to " + newPoint);

                    Thread.sleep(500);
                } else {
                    System.out.println("No valid move for FighterMonster at " + oldPoint);
                    Thread.sleep(500);
                }

            } catch (IllegalArgumentException | IllegalStateException e) {
                System.err.println("Race condition occurred: " + e.getMessage());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException interruptedException) {
                    model.getHall().removeObject(this.monster.getPosition());
                    model.spawnFighterMonster(this.monster.getPosition());
                    Thread.currentThread().interrupt();
                    System.err.println("Thread interrupted: " + interruptedException.getMessage());
                    break;
                }
            } catch (InterruptedException e) {

                model.getHall().removeObject(this.monster.getPosition());
                model.spawnFighterMonster(this.monster.getPosition());
                Thread.currentThread().interrupt();
                System.err.println("Thread interrupted: " + e.getMessage());
                break;
            }
        }
    }


    private void moveMonster(Point oldPoint, Point newPoint, String moveType) {
        synchronized (model.getHall().getEmptyPositions()) {
            if (!model.getHall().getEmptyPositions().contains(newPoint)) {
                System.err.println("Cannot move to occupied position: " + newPoint);
                return;
            }

            // Update the positions in the game model
            model.getHall().getGameObjects().remove(oldPoint);
            model.getHall().getGameObjects().put(newPoint, monster);
            model.getHall().getEmptyPositions().add(oldPoint);
            model.getHall().getEmptyPositions().remove(newPoint);

            // Move the monster and notify listeners
            model.getHall().moveObject(oldPoint, newPoint);
            monster.move(new ArrayList<>() {{
                add(newPoint);
            }});
            model.getHall().notifyListeners();
        }

        System.out.println("Fighter moved from " + oldPoint + " to " + newPoint + " " + moveType);
        sleepThread(500);
    }

    private void sleepThread(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
        }
    }
}
