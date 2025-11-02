package Model;

import java.util.ArrayList;

public class Pawn extends Piece{

    public Pawn(boolean isWhite){
        super(isWhite);
    }

    @Override
    public ArrayList<Position> possibleMoves(ArrayList<ArrayList<Piece>> board, Position pos){
        ArrayList<Position> possibleMoves = new ArrayList<>();
        // Determines direction of pawn (white upwards, black downwards)
        int direction = (isWhite) ? -1 : 1;

        int row = pos.row;
        int col = pos.col;

        // Checks forward movement of pawn
        int newRow = row + direction;
        if(isInBounds(newRow, col) && board.get(newRow).get(col) == null){
            possibleMoves.add(new Position(newRow, col));

            // Checks if pawn can move 2 forward
            int startingRow = isWhite ? 6 : 1;
            int doubleRow = row + 2 * direction;
            if(row == startingRow && board.get(doubleRow).get(col) == null){
                possibleMoves.add(new Position(doubleRow, col));
            }
        }

        // Checks diagonal left capture movement
        int diagonalColLeft = col - 1;
        if(isInBounds(newRow, diagonalColLeft)){
            Piece target =  board.get(newRow).get(diagonalColLeft);
            if(target != null && target.isWhite != this.isWhite){
                possibleMoves.add(new Position(newRow, diagonalColLeft));
            }
        }
        // Checks diagonal right capture movement
        int diagonalColRight = col + 1;
        if(isInBounds(newRow, diagonalColRight)){
            Piece target =  board.get(newRow).get(diagonalColRight);
            if(target != null && target.isWhite != this.isWhite){
                possibleMoves.add(new Position(newRow, diagonalColRight));
            }
        }

        return possibleMoves;
    }
}
