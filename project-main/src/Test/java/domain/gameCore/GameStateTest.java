package domain.gameCore;

import static domain.utilities.Constants.HallType.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import domain.behaviors.HallStrategy;
import domain.controllers.GameController;
import domain.gameCore.GameState;
import domain.gameObjects.*;
import domain.factories.MonsterFactory;
import domain.threads.CustomTimer;
import domain.utilities.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ui.swing.GameView;
import ui.swing.GameWindow;
import ui.swing.TimerDisplay;

import javax.swing.*;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class GameStateTest {

    @Nested
    class GameStateTest1 {
        private GameState gameState;
        private Hall hall;
        private MonsterFactory monsterFactory;
        private Player player;
        private GameController gameController;

        @BeforeEach
        public void setUp() {
            player = new Player(3, 3);
            gameController = new GameController();
            gameState = new GameState(player, gameController);
            TimerDisplay td = new TimerDisplay(30, gameState);
            gameController.setTimerDisplay(td);
            hall = gameState.getHall();
            monsterFactory = MonsterFactory.getInstance();
            gameState.setMonsterFactory(monsterFactory);
        }

        @Test
        public void testRepOk() {
            assertTrue(gameState.repOk(), "Representation invariant should hold");
        }

        @Test
        public void testSpawnMonster() {
            Point position = new Point(1, 1);

            gameState.spawnMonster(position);
            assertFalse(hall.getMonsters().isEmpty(), "Monster should be added to the hall");
        }

        @Test
        public void testSpawnWizardMonsterLimit() {
            Point position = new Point(1, 1);
            Monster wizardMonster = new WizardMonster(position.x, position.y);
            hall.addMonster(wizardMonster);

            gameState.spawnMonster(position);

            long wizardCount = hall.getMonsters().stream().filter(monster -> monster instanceof WizardMonster).count();
            assertEquals(1, wizardCount, "There should be only one wizard monster in the hall");
        }

        // 2 tests for different game objects
        @Test
        public void testInteractWithAdjacentChest() {
            Chest chest = new Chest(4, 3);
            hall.addObject(chest);

            assertTrue(gameState.interactWithObject(new Point(4, 3)));
        }

        @Test
        public void testInteractWithNonAdjacentWall() {
            Wall wall = new Wall(0, 2);
            hall.addObject(wall);

            assertFalse(gameState.interactWithObject(new Point(0, 2)));
        }

        // test for all possible directions for adjacency checks while keeping object the same
        @Test
        public void testInteractWithAdjacentDirections() {
            Block block = new Block(4, 3);
            hall.addObject(block);
            assertTrue(gameState.interactWithObject(new Point(4, 3)));
            block = new Block(2, 3);
            hall.addObject(block);
            assertTrue(gameState.interactWithObject(new Point(2, 3)));
            block = new Block(3, 4);
            hall.addObject(block);
            assertTrue(gameState.interactWithObject(new Point(3, 4)));
            block = new Block(3, 2);
            hall.addObject(block);
            assertTrue(gameState.interactWithObject(new Point(3, 2)));
        }

        @Test
        public void testInteractWithAdjacentLifeEnchantment() {
            LifeEnchantment lifeEnchantment = new LifeEnchantment(3, 4);
            hall.addObject(lifeEnchantment);
            player.setLifeCount(2);
            assertTrue(gameState.interactWithObject(new Point(3, 4)));
            assertEquals(3, player.getLifeCount());
        }

        @Test
        public void testInteractWithAdjacentTimeEnchantment() {
            TimeEnchantment timeEnchantment = new TimeEnchantment(3, 4);
            hall.addObject(timeEnchantment);
            assertTrue(gameState.interactWithObject(new Point(3, 4)));
            assertEquals(35.0f, gameState.getRemainingTime());
        }
        @Test
        public void testInteractWithAdjacentCloakEnchantment() {
            CloakEnchantment cloakEnchantment = new CloakEnchantment(3, 4);
            hall.addObject(cloakEnchantment);

            assertTrue(gameState.interactWithObject(new Point(3, 4)));
            assertTrue(player.getEnchantments().containsKey(cloakEnchantment));
        }
    }

    @Nested
    class GameStateTest2{
        private Player player;
        private GameController controller;
        private GameState gameState;
        private Hall hall;
        private HallStrategy hallStrategy;
        private Rune rune;
        private Map<Point, Rune> runeMap;
        private CustomTimer timer;
        private GameView gameView;
        private GameWindow gameWindow;
        private TimerDisplay timerDisplay;


        @BeforeEach
        public void setUp() {
            player = new Player(0,0);
            controller = new GameController();
            gameState = new GameState(player, controller);
            Runnable aVoid = null;
            timer = new CustomTimer(30,1000, aVoid);
            player = new Player(0, 0);
            gameWindow = new GameWindow();
            controller = new GameController(gameState, gameWindow, player);
            hallStrategy = new HallStrategy() {
                @Override
                public void setupHall(Hall hall) {
                }
                @Override
                public Constants.HallType getHallType() {
                    return EARTH; // Default to Earth
                }
                @Override
                public int getRequirements() {
                    return 6;
                }
                @Override
                public Hall getHall() {
                    return null;
                }
            };
            hall = new Hall("Test Hall", player, 30, EARTH);
            gameState = new GameState(player, controller, hallStrategy);
            gameState.setTimer(timer);
            timerDisplay = new TimerDisplay(30, gameState);
            controller.setTimerDisplay(timerDisplay);
            controller.setGameState(gameState);
            gameView = new GameView() {
                @Override
                public void initialize() {
                }
                @Override
                public void render() {
                }
                @Override
                public void teardown() {
                }
                @Override
                public JPanel getPanel() {
                    return null;
                }
            };
            controller.setView(gameView);
            JPanel panel = new JPanel();
            gameWindow.setView(panel);
            rune = new Rune(4, 4, EARTH);
            assertNotNull(rune);
            runeMap = new HashMap<>();
            runeMap.put(new Point(4, 4), rune);
        }


        @Test
        void testSuccessfulHallCompletionWithRune() {
            Point clickedPoint = new Point(4, 4);
            runeMap.put(clickedPoint, rune);
            Chest chest = new Chest(4, 4);
            hall.addObject(chest);
            hall.setRuneObjects(runeMap);
            gameState.setHall(hall);
            gameState.setTimer(timer);
            controller.setGameState(gameState);
            gameState.successfulHallCompletion(chest, clickedPoint, runeMap);
            assertTrue(rune.isRevealed());
        }

        @Test
        void testGameOverAfterFireHall() {
            hall.setHallType(FIRE);
            gameState.setHall(hall);
            Point clickedPoint = new Point(4, 4);
            Chest chest = new Chest(5, 5);
            gameState.successfulHallCompletion(chest, clickedPoint, runeMap);
            assertTrue(gameState.isGameOver());
        }

        @Test
        public void testGameStateInitialization_NullPlayer() {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                new GameState(null, controller);
            });
            assertEquals("Player cannot be null", thrown.getMessage());
        }
        @Test
        void testLevelTransitionFromEarthToAir() {
            hall.setHallType(EARTH);
            gameState.setHall(hall);

            Point clickedPoint = new Point(4, 4);
            Chest chest = new Chest(5, 5);
            gameState.successfulHallCompletion(chest, clickedPoint, runeMap);

            assertEquals(AIR, hall.getHallType());
        }

        @Test
        public void testIsGameOver() {
            assertFalse(gameState.isGameOver());
            gameState.gameOver();
            assertTrue(gameState.isGameOver());
        }


        @Test
        public void testGameStateInitialization_ValidInputs() {
            assertNotNull(gameState.getPlayer());
            assertNotNull(gameState.getController());
            assertTrue(gameState.getHall() instanceof Hall);
            assertEquals(player, gameState.getPlayer());
            assertEquals(controller, gameState.getController());
            assertTrue(gameState.getHall().getPlayer() == player);
        }
        @Test
        public void testGameOver() {
            // Test the gameOver method
            gameState.gameOver();
            assertTrue(gameState.isGameOver());
        }

        @Test
        void testLevelTransitionFromAirToWater() {
            // Simulate the Fire hall completion
            hall.setHallType(AIR);
            gameState.setHall(hall);

            // Call the successful hall completion
            Point clickedPoint = new Point(4,4);
            Chest chest = new Chest(5, 5); // Chest is a concrete subclass of GameObject
            gameState.successfulHallCompletion(chest, clickedPoint, runeMap);

            // Verify transition to Water hall (by checking hall type change)
            assertEquals(WATER, hall.getHallType());
        }

        @Test
        void testLevelTransitionFromWaterToFire() {
            // Simulate the Water hall completion
            hall.setHallType(WATER);
            gameState.setHall(hall);

            // Call the successful hall completion
            Point clickedPoint = new Point(4,4);
            Chest chest = new Chest(5, 5); // Chest is a concrete subclass of GameObject
            gameState.successfulHallCompletion(chest, clickedPoint, runeMap);

            // Verify transition to Air hall (by checking hall type change)
            assertEquals(FIRE, hall.getHallType());
        }

    }

}