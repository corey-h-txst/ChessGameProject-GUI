package Controller;

import java.io.*;
import java.util.LinkedList;
import Model.*;

public class MoveLog {
    private final LinkedList<String> moves;
    private final MoveHandler pieceMover;

    // Default constructor
    public MoveLog(MoveHandler pieceMover) {
        moves = new LinkedList<String>();
        this.pieceMover = pieceMover;
    }

    // Adds move to moves list
    public void addMove(String move) {
        moves.add(move);
    }
    // Removes the last added move from moves list
    public void removeLastMove() {
        // Checks if any moves have been made
        if(moves.isEmpty()) return;

        // Undoes the last move
        Position from = new Position(Character.getNumericValue(moves.getLast().charAt(0)), Character.getNumericValue(moves.getLast().charAt(1)));
        Position to = new Position(Character.getNumericValue(moves.getLast().charAt(2)), Character.getNumericValue(moves.getLast().charAt(3)));

        Piece movingPiece = pieceMover.getPieceAt(to);
        pieceMover.setPieceAt(from, movingPiece);
        pieceMover.setPieceAt(to, null);

        // Checks if a piece was taken
        if(moves.getLast().length() > 4) {
            // Gets deleted piece color
            boolean isWhite = moves.getLast().charAt(4) == 'w';
            // Gets deleted piece rank
            Piece piece = switch (moves.getLast().charAt(5)) {
                case 'q' -> new Queen(isWhite);
                case 'r' -> new Rook(isWhite);
                case 'b' -> new Bishop(isWhite);
                case 'n' -> new Knight(isWhite);
                default -> new Pawn(isWhite);
            };

            pieceMover.setPieceAt(to, piece);
        }
        // Removes move from moves list
        moves.removeLast();
    }
    // Clears the entire moves list (only used when loading save file or resetting board)
    public void clearLog(){
        moves.clear();
    }

    // Creates save state of a game by storing all executed moves (not including undoes)
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
                pieceMover.movePiece(from, to, true);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
