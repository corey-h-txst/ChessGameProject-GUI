package Controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import Model.Position;

public class MoveLog {
    private final List<String> moves;
    private final MoveHandler pieceMover;

    // Default constructor
    public MoveLog(MoveHandler pieceMover) {
        moves = new ArrayList<String>();
        this.pieceMover = pieceMover;
    }

    // Adds move to moves list
    public void addMove(String move) {
        moves.add(move);
    }
    // Removes the last added move from moves list
    public void removeLastMove() {
        moves.removeLast();
        // Add logic to undo last move as well
    }
    // Clears the entire moves list (only used when loading save file or reseting board)
    public void clearLog(){
        moves.clear();
    }

    // Creates save state of a game by storing all executed moves (not including undos)
    public void saveToFile(File file) throws IOException {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String move : moves) {
                writer.write(move);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadFromFile(File file) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                // Adds move to moves list
                addMove(line);
                // Executes move from log on chess board
                Position from = new Position(Character.getNumericValue(line.charAt(0)), Character.getNumericValue(line.charAt(1)));
                Position to = new Position(Character.getNumericValue(line.charAt(2)), Character.getNumericValue(line.charAt(3)));
                pieceMover.movePiece(from, to);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
