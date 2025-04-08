/**
 * Adapter for handling game persistence using file storage.
 * Implements the `SaveGameAdapter` interface to provide methods for saving, loading, listing, and deleting game states.
 * Utilizes the Jackson library for JSON serialization and deserialization.
 */

package technicalServices.persistence.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import domain.controllers.GameController;
import domain.dto.CollectedEnchantmentDto;
import domain.dto.GameStateDto;
import domain.gameCore.GameState;
import domain.gameObjects.Rune;
import domain.serializers.*;
import technicalServices.logging.LogManager;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileAdapter implements SaveGameAdapter{
    private static final String SAVE_GAME_PATH = "src/main/technicalServices/persistence/savedGames/";
    private static final String FILE_EXTENSION = ".json";
    private final GameStateSerializer serializer;

    //this is from jackson library, installation is needed
    private final ObjectMapper objectMapper;
    public FileAdapter(GameStateSerializer serializer) {
        this.serializer = serializer;
        this.objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Point.class, new PointSerializer());
        module.addDeserializer(Point.class, new PointDeserializer());
        module.addSerializer(Rune.class, new RuneSerializer());
        module.addDeserializer(Rune.class, new RuneDeserializer());
        module.addKeyDeserializer(Point.class, new PointKeyDeserializer());
        module.addKeyDeserializer(CollectedEnchantmentDto.class, new CollectedEnchantmentDtoKeyDeserializer());
        module.addDeserializer(CollectedEnchantmentDto.class, new CollectedEnchantmentDtoDeserializer());
        module.addSerializer(CollectedEnchantmentDto.class, new CollectedEnchantmentDtoSerializer());
        objectMapper.registerModule(module);
        Path path = Paths.get(SAVE_GAME_PATH);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("Could not create save directory.", e);
            }
        }
    }
    /**
     * Saves the given game state to a file with the specified name.
     *
     * @param gameName  The name of the saved game.
     * @param gameState The current state of the game to be saved.
     * @throws RuntimeException If the game name or game state is null or if an I/O error occurs during saving.
     */

    @Override
    public void saveGame(String gameName, GameState gameState) {
        if (gameName == null || gameName.trim().isEmpty()) {
            throw new RuntimeException("Game name cannot be null or empty.");
        }
        if (gameState == null) {
            throw new RuntimeException("Game state cannot be null.");
        }
        GameStateDto gameStateDto = serializer.serializeGameState(gameState);
        String fileName = SAVE_GAME_PATH + File.separator + gameName + FILE_EXTENSION;
        try {
            objectMapper.writeValue(new File(fileName), gameStateDto);
            LogManager.logInfo("Game saved successfully: " + gameName);
            System.out.println("Game saved successfully: " + gameName);
        } catch (IOException e) {
            throw new RuntimeException("Error saving game: " + gameName, e);
        }

    }
    /**
     * Loads a saved game state from a file with the specified name.
     *
     * @param gameName The name of the saved game to load.
     * @return The loaded `GameState` object.
     * @throws RuntimeException If the file cannot be read or deserialization fails.
     */

    @Override
    public GameState loadGame(String gameName) {
        String fileName = SAVE_GAME_PATH + File.separator + gameName + FILE_EXTENSION;
        try {
            GameStateDto gameStateDto = objectMapper.readValue(new File(fileName), GameStateDto.class);
            return serializer.deserializeGameState(gameStateDto, new GameController());
        } catch (IOException e) {
            throw new RuntimeException("Error loading game: " + gameName, e);
        }
    }
    /**
     * Lists all saved games available in the save directory.
     *
     * @return A list of saved game names.
     */

    @Override
    public List<String> listSavedGames() {
        File directory = new File(SAVE_GAME_PATH);
        File[] files = directory.listFiles((d, name) -> name.endsWith(FILE_EXTENSION));
        List<String> savedGames = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                savedGames.add(file.getName().replace(FILE_EXTENSION, ""));
            }
        }
        return savedGames;
    }
    /**
     * Deletes the saved game file with the specified name.
     *
     * @param gameName The name of the saved game to delete.
     * @throws RuntimeException If an I/O error occurs during deletion.
     */

    @Override
    public void deleteSavedGame(String gameName) {
        String fileName = SAVE_GAME_PATH + File.separator + gameName + FILE_EXTENSION;
        Path path = Paths.get(fileName);
        try {
            Files.deleteIfExists(path);
            LogManager.logInfo("Game deleted successfully: " + gameName);
            System.out.println("Game deleted successfully: " + gameName);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting saved game: " + gameName, e);
        }
    }
}
