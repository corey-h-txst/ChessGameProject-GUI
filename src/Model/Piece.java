package Model;

import java.util.ArrayList;

public abstract class Piece {
    public boolean isWhite;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    // Default possibleMoves function which is overridden in all subclasses with piece-specific behavior
    public ArrayList<Position> possibleMoves(ArrayList<Piece> board){
        return new ArrayList<Position>();
    }
}
