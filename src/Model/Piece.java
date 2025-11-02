package Model;

import java.util.ArrayList;

public abstract class Piece {
    public boolean isWhite;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    // Default possibleMoves function which is overridden in all subclasses with piece-specific behavior
    public ArrayList<Position> possibleMoves(ArrayList<ArrayList<Piece>> board, Position pos){
        return new ArrayList<Position>();
    }
    // Helper function for possibleMoves to limit bounds when testing moves
    protected boolean isInBounds(int row, int col){
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
}
