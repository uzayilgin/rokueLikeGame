package technicalServices.persistence.adapters;

import domain.gameCore.GameState;

import java.util.List;

public interface SaveGameAdapter {
    void  saveGame(String gameName, GameState gameState);
    GameState loadGame(String gameName);
    List<String> listSavedGames();

    void deleteSavedGame(String gameName);
}
