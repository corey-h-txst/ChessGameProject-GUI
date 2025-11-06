package Controller;

import View.GUI;

import java.io.File;
import java.io.IOException;

public class GameController {
    public final MoveLog gameLog;
    private final GUI gui;
    private final MoveHandler game;

    // Default constructor
    public GameController(GUI gui, MoveHandler game) {
        gameLog = new MoveLog(game);
        this.gui = gui;
        this.game = game;
    }

    // Resets the board, clears the log, and clears the move log panel
    public void newGame() {
        game.resetBoard();
        gameLog.clearLog();
        gui.logText.setText("");
        gui.updateBoardGUI();
    }
    // Takes a file name from GUI and saves the current log to a file in SaveFiles directory
    public void saveGame(String saveFileName) throws IOException {
        File saveFile = new File("SaveFiles/" + saveFileName);
        gameLog.saveToFile(saveFile);
    }
    // Takes in file name from GUI and loads the selected file by creating a new game and then re-executing all moves from log file
    public void loadGame(String loadFileName) throws IOException {
        File loadFile = new File("SaveFiles/" + loadFileName);
        newGame();
        gameLog.loadFromFile(loadFile);
    }
}
