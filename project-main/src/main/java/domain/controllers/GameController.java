/**
 * The GameController class manages the core logic and interactions of the game.
 *
 * It controls the game flow, transitions between screens, game state management, and interactions between the player,
 * hall, and other game entities.
 */
package domain.controllers;

import assets.audio.SoundProcessor;
import domain.behaviors.*;
import domain.factories.EnchantmentFactory;
import domain.factories.MonsterFactory;
import domain.gameCore.GameSession;
import domain.gameCore.GameState;
import domain.gameObjects.*;
import domain.observers.HealthObserver;
import domain.threads.CustomTimer;
import domain.threads.HallManager;
import domain.utilities.*;
import technicalServices.logging.LogManager;
import ui.gameObjectImage.*;
import ui.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.awt.image.BufferedImage;
import java.util.Map;

import java.util.ArrayList;

public class GameController implements Serializable {
    private GameState gameState;
    private GameView view;
    private GameWindow window;
    private Player player;
    private boolean isPaused = false;
    private PlayerController playerController;
    private BufferedImage snapshot; //last appearance of the play mode screen
    private JPanel[][] playModeGridPanels;
    private HealthHeartDisplay healthHeartDisplay;
    private HallManager hallManager;

    private TimerDisplay timerDisplay;
    /**
     * Constructs a GameController with the specified game state, game window, and player.
     *
     * @param gameState the current state of the game
     * @param window the main game window
     * @param player the player object
     */
    public GameController(GameState gameState, GameWindow window, Player player) {
        this.gameState = gameState;
        this.window = window;
        this.player = player;
        this.playerController = new PlayerController(gameState);
        this.snapshot = null;
        this.healthHeartDisplay = new HealthHeartDisplay(player, this);
        LogManager.logInfo("GameController initialized. [from class: GameController, method: GameController]");
        System.out.println("GameController initialized.");
    }

    public PlayerController getPlayerController() {
        return playerController;
    }
    /**
     * Default constructor used for deserialization.
     * Initializes with default values and sets the player controller.
     */
    //this is for deserialization please do not delete!
    public GameController() {
        //set the gamestate to newly loaded one in the loaded game part
        this.playerController = new PlayerController(gameState);
        LogManager.logInfo("GameController initialized with empty constructor. [from class: GameController, method: GameController]");
        System.out.println("GameController initialized with empty constructor.");
    }
    public HallManager getHallManager() {
        return this.hallManager;
    }
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setView(GameView view) {
        this.view = view;
    }

    public BufferedImage setSnapshot(BufferedImage snapshot) {
        this.snapshot = snapshot;
        return this.snapshot;
    }

    public GameView getView() {
        return this.view;
    }
    /**
     * Pauses the game by freezing the game state and stopping relevant timers and threads.
     */
    public void pauseGame() {
        playerController.setPaused(true);
        isPaused = true;
        gameState.freeze();
        LogManager.logInfo("Game paused. [from class: GameController, method: pauseGame]");
        System.out.println("Game paused.");
        timerDisplay.stopTimer();
    }
    /**
     * Resumes the game by unfreezing the game state and restarting timers.
     */
    public void resumeGame() {
        playerController.setPaused(false);
        isPaused = false;
        gameState.unfreeze();
        LogManager.logInfo("Game resumed. [from class: GameController, method: resumeGame]");
        System.out.println("Game resumed.");
        timerDisplay.startTimer();
    }
    /**
     * Stops the game entirely, freezing the game state and stopping all threads and timers.
     */
    public void stopGame() {
        playerController.setPaused(true);
        isPaused = true;
        HallManager.stopAllThreads();
        gameState.freeze();
        LogManager.logInfo("Game over. [from class: GameController, method: stopGame]");
        System.out.println("Game over.");
        timerDisplay.stopTimer();
    }
    /**
     * Stops the game entirely, freezing the game state and stopping all threads and timers. New sound is generated
     * to indicate successful ending.
     */
    public void successfulEnding() {
        isPaused = true;
        gameState.freeze();
        LogManager.logInfo("Game successfully ended. [from class: GameController, method: successfulEnding]");
        System.out.println("Game successfully ended.");
        timerDisplay.stopTimer();

        SoundProcessor soundProcessor = new SoundProcessor("src/main/java/assets/audio/undertale-sound-effect-you-win.wav");
        soundProcessor.playSound();
        onGameSuccess(setSnapshot(snapshot));
    }

    public boolean isGamePaused() {
        return isPaused;
    }

    public void notifyGameOver() {
        LogManager.logInfo("Game over. [from class: GameController, method: notifyGameOver]");
        onGameOver(setSnapshot(snapshot));
    }

    public Player getPlayerObject() {
        return player;
    }

    /**
     * Starts the game and transitions to the main menu screen.
     *
     * This method logs the start of the game and displays the main menu,
     * where players can choose actions such as starting a new game or loading a saved game.
     */
    public void startGame() {
        LogManager.logInfo("Game started. [from class: GameController, method: startGame]");
        System.out.println("Game started.");
        switchToMainMenu();
    }
    /**
     * Switches the current view to the Earth Hall screen.
     *
     * This method sets the hall type to Earth, initializes the Earth Hall screen,
     * and configures key listeners for interaction. When the ENTER key is pressed,
     * the screen transitions to the building phase for the Earth Hall.
     */
    public void switchToEarthHallScreen() {
        if (view != null) {
            view.teardown();
        }
        gameState.getHall().setHallType(Constants.HallType.EARTH);

        EarthHallScreen earthHallScreen = new EarthHallScreen();
        earthHallScreen.initialize();
        view = earthHallScreen;
        window.setView(view.getPanel());

        JPanel panel = earthHallScreen.getPanel();
        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    window.closeW();
                    new GameSession(new EarthHallStrategy(player)).startAtBuilding();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        // Ensure the panel can receive focus
        panel.setFocusable(true);
        panel.requestFocusInWindow();
    }
    /**
     * Switches the current view to the Fire Hall screen.
     *
     * This method sets the hall type to Fire, initializes the Fire Hall screen,
     * and configures key listeners for interaction. When the ENTER key is pressed,
     * the screen transitions to the building phase for the Fire Hall.
     */
    public void switchToFireHallScreen() {
        if (view != null) {
            view.teardown();
        }
        gameState.getTimer().stop();
        gameState.getHall().setHallType(Constants.HallType.FIRE);
        gameState.setHallStrategy(new FireHallStrategy(player));
        FireHallScreen fireHallScreen = new FireHallScreen();
        fireHallScreen.initialize();
        view = fireHallScreen;
        window.setView(view.getPanel());

        JPanel panel = fireHallScreen.getPanel();
        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    window.closeW();
                    new GameSession(new FireHallStrategy(player)).startAtBuilding();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        panel.setFocusable(true);
        panel.requestFocusInWindow();
    }
    /**
     * Switches the current view to the Water Hall screen.
     *
     * This method sets the hall type to Water, initializes the Water Hall screen,
     * and configures key listeners for interaction. When the ENTER key is pressed,
     * the screen transitions to the building phase for the Water Hall.
     */
    public void switchToWaterHallScreen() {
        if (view != null) {
            view.teardown();
        }

        gameState.getHall().setHallType(Constants.HallType.WATER);
        gameState.getTimer().stop();
        gameState.setHallStrategy(new WaterHallStrategy(player));
        WaterHallScreen waterHallScreen = new WaterHallScreen();
        waterHallScreen.initialize();
        view = waterHallScreen;
        window.setView(view.getPanel());

        JPanel panel = waterHallScreen.getPanel();
        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    window.closeW();
                    new GameSession(new WaterHallStrategy(player)).startAtBuilding();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        panel.setFocusable(true);
        panel.requestFocusInWindow();
    }
    /**
     * Switches the current view to the Air Hall screen.
     *
     * This method sets the hall type to Air, initializes the Air Hall screen,
     * and configures key listeners for interaction. When the ENTER key is pressed,
     * the screen transitions to the building phase for the Air Hall.
     */
    public void switchToAirHallScreen() {
        if (view != null) {
            view.teardown();
        }
        gameState.getHall().setHallType(Constants.HallType.AIR);
        gameState.getTimer().stop();
        gameState.setHallStrategy(new AirHallStrategy(player));
        AirHallScreen airHallScreen = new AirHallScreen();
        airHallScreen.initialize();
        view = airHallScreen;
        window.setView(view.getPanel());

        JPanel panel = airHallScreen.getPanel();
        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    window.closeW();
                    new GameSession(new AirHallStrategy(player)).startAtBuilding();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        panel.setFocusable(true);
        panel.requestFocusInWindow();
    }
    /**
     * Places a Rune randomly within the hall at a valid location.
     *
     * This method identifies suitable locations for the Rune, such as positions
     * occupied by walls, chests, blocks, or other valid objects. If a valid position
     * is found, a Rune is placed at a randomly selected location and added to the hall.
     *
     * @param hall the hall where the Rune will be placed
     */
    private void placeRuneRandomlyInHall(Hall hall) {
        ArrayList<Point> objectLocations = new ArrayList<>();

        for (Point point : hall.getGameObjects().keySet()) {
            GameObject obj = hall.getGameObjects().get(point);
            if (obj instanceof Chest || obj instanceof Wall || obj instanceof WallDifferent || obj instanceof Block) {
                objectLocations.add(point);
            }
        }

        if (!objectLocations.isEmpty()) {
            Point randomLocation = objectLocations.get((int) (Math.random() * objectLocations.size()));
            int row = randomLocation.y;
            int col = randomLocation.x;

            Rune rune = new Rune(col, row, hall.getHallType());
            hall.addObject(rune);
            hall.getRuneObjects().put(randomLocation, rune);

            LogManager.logInfo("Rune placed at " + row + "," + col + " [from class: GameController, method: placeRuneRandomlyInHall]");
            System.out.println("Rune placed at " + row + "," + col);
        } else {
            LogManager.logError("No valid locations to place the Rune. [from class: GameController, method: placeRuneRandomlyInHall]");
            System.err.println("No valid locations to place the Rune.");
        }
    }
    /**
     * Switches the current view to the main menu screen.
     *
     * This method stops all threads related to the hall and transitions to the main menu.
     * The main menu provides options for starting a new game, loading saved games, viewing help,
     * or exiting the application.
     */
    public void switchToMainMenu() {
        if (view != null) {
            view.teardown();
        }

        HallManager.stopAllThreads();
        MainMenuScreen mainMenu = new MainMenuScreen();
        mainMenu.initialize();
        mainMenu.getStartButton().addActionListener(e -> switchToEarthHallScreen());
        mainMenu.getSavedGamesButton().addActionListener(e -> switchToSavedGamesScreen());
        mainMenu.getHelpButton().addActionListener(e -> showHelpScreen());
        mainMenu.getExitButton().addActionListener(e -> System.exit(0));

        view = mainMenu;
        window.setView(view.getPanel());
    }
    /**
     * Switches the current view to the saved games screen.
     *
     * This method loads the list of saved games and displays them to the user. The user
     * can choose to delete a saved game, load a selected saved game, or return to the main menu.
     */
    public void switchToSavedGamesScreen() {
        // i will add 2 methods to gamestate
        //but gamestate will communicate with adapter interface
        ArrayList<String> savedGames = gameState.loadSavedGames();
        SavedGamesScreen savedGamesScreen = new SavedGamesScreen(savedGames);
        this.setView(savedGamesScreen);
        savedGamesScreen.initialize();
        savedGamesScreen.getDeleteSelectedButton().addActionListener(e -> {
            String selectedGame = savedGamesScreen.getSelectedGame();
            if (selectedGame != null) {
                deleteSavedGame(selectedGame);
                ArrayList<String> updatedSavedGames = gameState.loadSavedGames();
                savedGamesScreen.setSavedGames(updatedSavedGames);
                savedGamesScreen.render();
            } else {
                JOptionPane.showMessageDialog(window, "Please select a game to delete.", "No Game Selected", JOptionPane.WARNING_MESSAGE);
            }
        });
        savedGamesScreen.getLoadButton().addActionListener(e -> {
            {
                String selectedGame = savedGamesScreen.getSelectedGame();
                if (selectedGame != null) {
                    gameState.loadGame(selectedGame);
                    switchToLoadedGame();
                } else {
                    JOptionPane.showMessageDialog(window, "Please select a game to load.", "No Game Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        savedGamesScreen.getCancelButton().addActionListener(e -> switchToMainMenu());

        view = savedGamesScreen;
        window.setView(view.getPanel());
    }
    /**
     * Deletes a saved game by its name.
     *
     * This method removes a saved game from storage.
     *
     * @param gameName the name of the saved game to delete
     */
    public void deleteSavedGame(String gameName) {
        gameState.deleteSavedGame(gameName);
    }
    /**
     * Transitions to the build mode, allowing players to set up their hall.
     *
     * @param hallStrategy the strategy for the hall to be built
     */
    public void switchToBuildMode(HallStrategy hallStrategy) {



        LogManager.logInfo("Switching to Build Mode. [from class: GameController, method: switchToBuildMode]");
        System.out.println("Switching to Build Mode.");
        if (view != null) {
            view.teardown();
        }

        BuildMode buildMode = new BuildMode(hallStrategy);
        buildMode.initialize();

        buildMode.getDeleteButton().addActionListener(e -> {
            if (buildMode.isPlacementPhase()) {
                buildMode.selectObject("Delete");
            }
        });

        buildMode.getSubmitButton().addActionListener(e -> {
            int confirmation = buildMode.showConfirmDialog();

            if (confirmation == JOptionPane.YES_OPTION) {
                boolean isValid = buildMode.validateHall();
                if (isValid) {
                    buildMode.setPlacementPhase(false);
                    String[][] placedObjects = buildMode.getPlacedObjects();
                    switchToPlayMode(placedObjects, hallStrategy);
                } else {
                    LogManager.logError("Object number validation failed. [from class: GameController, method: switchToBuildMode]");
                    System.out.println("Object number validation failed.");
                    JOptionPane.showMessageDialog(buildMode.getPanel(),
                            "Object number validation failed! Please adjust your placement.");

                    buildMode.getPanel().requestFocusInWindow();
                }
            }
        });

        for (JButton objButton : buildMode.getObjectButtons()) {
            objButton.addActionListener(ev -> {
                if (buildMode.isPlacementPhase()) {
                    buildMode.selectObject(objButton.getText());
                }
            });
        }

        view = buildMode;
        window.setView(view.getPanel());
    }
    /**
     * Transitions to the play mode, starting the main gameplay loop.
     *
     * @param placedObjects the layout of objects placed in the hall
     * @param hallStrategy the strategy for the hall being played
     */
    public void switchToPlayMode(String[][] placedObjects, HallStrategy hallStrategy) {
        LogManager.logInfo("Switching to Play Mode. [from class: GameController, method: switchToPlayMode]");
        System.out.println("Switching to Play Mode.");
        if (view != null) {
            view.teardown();
        }

        Hall hall = createHallFromPlacedObjects(placedObjects);
        placeRuneRandomlyInHall(hall);
        hall.setPlayer(player);
        gameState.setHall(hall);

        int numPlacedObjects = 0;
        for (String[] row : placedObjects) {
            for (String object : row) {
                if (object != null && !object.isEmpty()) {
                    numPlacedObjects++;
                }
            }
        }

        int timerDuration = numPlacedObjects * 5;

        gameState.setTimer(new CustomTimer(timerDuration));
        PlayMode playMode = new PlayMode(this, playerController, hallStrategy);
        playMode.initialize();
        view = playMode;
        playMode.getSaveGameButton().addActionListener(e -> {
            //we should pause the game before saving so that the game state is not changed while saving
            this.saveGameActionListener();
        });
        JPanel gamePanel = playMode.getPanel();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());


        timerDisplay = new TimerDisplay(timerDuration, getGameState());
        mainPanel.add(timerDisplay.getPanel(), BorderLayout.NORTH);

        mainPanel.add(gamePanel, BorderLayout.CENTER);

        window.setContentPane(mainPanel);
        window.revalidate();
        window.repaint();

        timerDisplay.setListener(new TimerDisplay.TimerListener() {
            @Override
            public void onTimeUp() {
                pauseGame();
                LogManager.logInfo("Time is over, so the game is over. [from class: GameController, method: switchToPlayMode]");
                System.out.println("Time is over, so the game is over.");
                onGameOver(setSnapshot(snapshot));
            }
        });

        timerDisplay.startTimer();

        MonsterController monsterController = new MonsterController(gameState, MonsterFactory.getInstance());
        EnchantmentController enchantmentController = new EnchantmentController(gameState, EnchantmentFactory.getInstance());
        HallManager hallManager = new HallManager(gameState, monsterController, enchantmentController);
        this.hallManager = hallManager;
        Thread hallManagerThread = new Thread(hallManager);
        hallManagerThread.start();
    }
    /**
     * Switches the current game state to a loaded game.
     *
     * This method retrieves the saved game state, uses the appropriate hall strategy,
     * initializes the play mode, and sets up the user interface for the loaded game.
     *
     * The method ensures that the game controller is properly set within the game state
     * after deserialization. It also starts the timer and hall manager threads, resuming
     * the game from the saved state.
     */
    public void switchToLoadedGame() {
        LogManager.logInfo("Switching to Loaded Game. [from class: GameController, method: switchToLoadedGame]");
        System.out.println("Switching to Loaded Game.");

        if (view != null) {
            view.teardown();
        }
        HallStrategy hallStrategy = new EarthHallStrategy(player);
        Hall hall = gameState.getHall();
        if (hall.getHallType() == Constants.HallType.EARTH) {
            hallStrategy = new EarthHallStrategy(player);
        }else if (hall.getHallType() == Constants.HallType.FIRE) {
            hallStrategy = new FireHallStrategy(player);
        }else if (hall.getHallType() == Constants.HallType.WATER) {
            hallStrategy = new WaterHallStrategy(player);
        }else if (hall.getHallType() == Constants.HallType.AIR) {
            hallStrategy = new AirHallStrategy(player);
        }
        Player loadedPlayer = gameState.getPlayer();
        this.setGameState(gameState);
        this.setPlayer(loadedPlayer);

        this.playerController = new PlayerController(gameState);
        PlayMode playMode = new PlayMode(this, this.playerController, hallStrategy);
        playMode.initialize();
        this.setView(playMode);
        playMode.setSaveDate(gameState.getSaveDate());
        //doing this is critical bc game controller is not saved to json
        //after doing necessary sets in the controller we need to ensure that the game states controller is this one
        gameState.setController(this);
        playMode.getSaveGameButton().addActionListener(e -> {
            this.saveGameActionListener();
        });
        JPanel gamePanel = playMode.getPanel();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        this.setTimerDisplay(new TimerDisplay(hall.getTimeRemaining(), gameState));
        mainPanel.add(timerDisplay.getPanel(), BorderLayout.NORTH);
        mainPanel.add(gamePanel, BorderLayout.CENTER);

        window.setContentPane(mainPanel);
        window.revalidate();
        window.repaint();

        timerDisplay.setListener(new TimerDisplay.TimerListener() {
            @Override
            public void onTimeUp() {
                stopGame();
                LogManager.logInfo("Time is over, so the game is over. [from class: GameController, method: switchToLoadedGame]");
                System.out.println("Time is over, so the game is over.");
                onGameOver(setSnapshot(snapshot));
            }
        });

        timerDisplay.startTimer();

        MonsterController monsterController = new MonsterController(gameState, MonsterFactory.getInstance());
        HallManager hallManager = new HallManager(gameState, monsterController, new EnchantmentController(gameState, EnchantmentFactory.getInstance()));
        Thread hallManagerThread = new Thread(hallManager);
        hallManagerThread.start();
        this.resumeGame();
        SwingUtilities.invokeLater(() -> {
            JPanel playPanel = ((PlayMode) this.view).getPanel();
            playPanel.requestFocusInWindow();
        });
    }
    public void setTimerDisplay(TimerDisplay timerDisplay) {
        this.timerDisplay = timerDisplay;
    }

    public TimerDisplay getTimerDisplay() {
        return timerDisplay;
    }
    /**
     * Saves the current game state with a specified name.
     */
    public void saveGameActionListener() {
        this.pauseGame();
        String gameName = JOptionPane.showInputDialog(window, "Enter a name for your saved game:", "Save Game", JOptionPane.PLAIN_MESSAGE);
        if (gameName != null && !gameName.trim().isEmpty()) {
            gameState.saveGame(gameName);
            SoundProcessor soundProcessor = new SoundProcessor("src/main/java/assets/audio/undertale-save.wav");
            soundProcessor.playSound();
        } else {
            JOptionPane.showMessageDialog(window, "Game name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Handles the successful ending of the game, plays a sound, and shows a success screen.
     *
     * @param snapshot a snapshot of the game screen at the moment of success
     */
    public void onGameSuccess(BufferedImage snapshot) {
        stopGame();

        SuccessMiniScreen successScreen = new SuccessMiniScreen();
        successScreen.initialize();

        if (snapshot == null) {
            snapshot = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);  // default empty image
        }

        JLabel backgroundLabel = new JLabel(new ImageIcon(snapshot));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        successScreen.getPanel().add(backgroundLabel, gbc);

        window.setContentPane(successScreen.getPanel());
        window.revalidate();

        successScreen.onGameSuccess();

        successScreen.setMainMenuAction(e -> new GameSession().startGame());
    }
    public void startBuildingMode(HallStrategy hallStrategy) {
        window.closeW();
        if (hallStrategy.getHallType() == Constants.HallType.EARTH) {
            switchToEarthHallScreen();
        } else if (hallStrategy.getHallType() == Constants.HallType.FIRE) {
            switchToFireHallScreen();
        } else if (hallStrategy.getHallType() == Constants.HallType.WATER) {
            switchToWaterHallScreen();
        } else if (hallStrategy.getHallType() == Constants.HallType.AIR) {
            switchToAirHallScreen();
        }

    }
    public void startMainMode() {
        window.closeW();
        new GameSession().startGame();
    }
    /**
     * Starts the game in the specified hall mode.
     *
     * This method closes the current game window and transitions to the screen
     * corresponding to the provided hall type. It initializes the game session
     * with the appropriate hall strategy and starts the game.
     *
     * @param hallStrategy the type of hall to start the game in (EARTH, FIRE, WATER, or AIR)
     */
    public void startHallMode(Constants.HallType hallStrategy) {
        window.closeW();

        if (hallStrategy == Constants.HallType.EARTH) {
            switchToEarthHallScreen();
            new GameSession().startGame();

        } else if (hallStrategy == Constants.HallType.FIRE) {
            switchToFireHallScreen();
            new GameSession(new FireHallStrategy(player)).startGame();

        } else if (hallStrategy == Constants.HallType.WATER) {
            switchToWaterHallScreen();
            new GameSession(new WaterHallStrategy(player)).startGame();

        } else {
            switchToAirHallScreen();
            new GameSession(new AirHallStrategy(player)).startGame();

        }
    }

    /**
     * Handles the game-over logic, plays a sound, and shows a game-over screen.
     *
     * @param snapshot a snapshot of the game screen at the moment of game over
     */
    public void onGameOver(BufferedImage snapshot) {
        stopGame();

        SoundProcessor soundProcessor = new SoundProcessor("src/main/java/assets/audio/game-over.wav");
        soundProcessor.playSound();
        GameOverMiniScreen gameOverScreen = new GameOverMiniScreen();
        gameOverScreen.initialize();

        if (snapshot == null) {
            snapshot = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);  // default empty image
        }

        JLabel backgroundLabel = new JLabel(new ImageIcon(snapshot));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gameOverScreen.getPanel().add(backgroundLabel, gbc);

        window.setContentPane(gameOverScreen.getPanel());
        window.revalidate();

        gameOverScreen.onGameOver();
        gameOverScreen.setTryAgainAction(e -> startHallMode(gameState.getHall().getHallType()));

        gameOverScreen.setMainMenuAction(e -> startMainMode());
        gameOverScreen.setHelpAction(e -> showHelpScreen());
    }
    /**
     * Displays the help screen to the player.
     *
     * This method transitions the current view to the help screen, where players
     * can find information about how to play the game. It also sets up a close button
     * action that returns to the main menu when clicked.
     */
    public void showHelpScreen() {
        LogManager.logInfo("Showing Help Screen. [from class: GameController, method: showHelpScreen]");
        System.out.println("Showing Help Screen.");
        if (view != null) {
            view.teardown();
        }

        HelpScreen helpScreen = new HelpScreen();
        helpScreen.initialize();

        helpScreen.getCloseButton().addActionListener(e -> startMainMode());

        view = helpScreen;
        window.setView(view.getPanel());
    }
    /**
     * Creates a Hall object based on the provided 2D array of placed objects.
     *
     * This method iterates through the array of placed objects and generates
     * corresponding GameObjects instances based on their names. Each object
     * is added to the hall, and it is ensured that no duplicates are placed at the same position.
     *
     * @param placedObjects a 2D array representing the layout of objects in the hall
     * @return the created Hall object populated with the specified objects
     */
    private Hall createHallFromPlacedObjects(String[][] placedObjects) {
        LogManager.logInfo("Creating hall from placed objects. [from class: GameController, method: createHallFromPlacedObjects]");
        System.out.println("Creating hall from placed objects.");

        Hall hall = gameState.getHall();

        for (int r = 0; r < 16; r++) {
            for (int c = 0; c < 16; c++) {
                String objName = placedObjects[r][c];

                if (objName != null) {
                    GameObject obj = createGameObjectFromName(objName, c, r);

                    if (obj != null && !hall.getGameObjects().containsKey(new Point(c, r))) {
                        hall.addObject(obj);
                    }
                }
            }
        }

        return hall;
    }

    private Point findEmptySpot(Hall hall) {
        Map<Point, GameObject> gameObjects = hall.getGameObjects();
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 16; col++) {
                Point position = new Point(col, row);
                if (!gameObjects.containsKey(position)) {
                    return position;
                }
            }
        }
        LogManager.logError("No empty spots available in the hall. [from class: GameController, method: findEmptySpot]");
        throw new IllegalStateException("No empty spots available in the hall.");
    }

    public GameState getGameState() {
        return gameState;
    }
    /**
     * Retrieves the appropriate renderer for a given game object.
     *
     * This method checks the type of the provided {@code GameObject} and returns
     * the corresponding GameEntityImage instance, which is used to render the object
     * in the game view.
     *
     * @param object the game object for which the renderer is required
     * @return a GameEntityImage instance for rendering the object, or null if no match is found
     */
    public GameEntityImage getGameEntityRenderer(GameObject object) {
        if (object instanceof Player) {
            return new PlayerImage();
        } else if (object instanceof Monster) {
            return new MonsterImage();
        } else if (object instanceof Enchantment) {
            return new EnchantmentImage();
        } else if (object instanceof Chest) {
            return new ChestImage();
        } else if (object instanceof Wall) {
            return new WallImage();
        } else if (object instanceof Block) {
            return new BlockImage();
        } else if (object instanceof WallDifferent) {
            return new WallDifferentImage();
        }
        return null;
    }
    /**
     * Updates the play mode grid to display a Rune icon at the specified position.
     *
     * This method updates the grid panel in the play mode view by setting the icon
     * of the grid cell at the given clickedPoint to the provided runeIcon.
     * After updating, the method triggers a re-rendering of the grid and refreshes the window.
     *
     * @param clickedPoint the position on the grid where the Rune icon will be displayed
     * @param runeIcon the ImageIcon to display in the specified grid cell
     */
    public void updatePlayModeGrid(Point clickedPoint, ImageIcon runeIcon) {
        if (view instanceof PlayMode) {
            PlayMode playMode = (PlayMode) view;

            JPanel[][] gridPanels = playMode.getGridPanels();
            JPanel objectPanel = gridPanels[clickedPoint.y][clickedPoint.x];
            Component[] components = objectPanel.getComponents();
            for (Component component : components) {
                if (component instanceof JLabel) {
                    ((JLabel) component).setIcon(runeIcon);
                    break;
                }
            }
            System.out.println("Rune is shown on the grid.");
            LogManager.logInfo("Play mode grid updated. [from class: GameController, method: updatePlayModeGrid]");
            playMode.render();
            window.revalidate();
            window.repaint();
        }
    }
    /**
     * Creates a game object based on its name and coordinates.
     *
     * This method maps a string name to a specific GameObject type and creates
     * an instance of the corresponding object at the specified coordinates. If the name
     * does not match any recognized object type, the method returns null.
     *
     * @param name the name of the game object to create
     * @param x the x-coordinate where the object will be placed
     * @param y the y-coordinate where the object will be placed
     * @return the created GameObject, or null if the name does not match any type
     */
    private GameObject createGameObjectFromName(String name, int x, int y) {
        switch (name) {
            case "Chest":
                return new Chest(x, y);
            case "Wall":
                return new Wall(x, y);
            case "Block":
                return new Block(x, y);
            case "Diff Wall":
                return new WallDifferent(x, y);
            default:
                return null;
        }
    }
    public HealthHeartDisplay getHealthHeartDisplay() {
        return healthHeartDisplay;
    }
}