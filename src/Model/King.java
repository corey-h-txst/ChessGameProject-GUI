package Model;

import java.util.ArrayList;

public class King extends Piece{

    public King(boolean isWhite) {super(isWhite);}

    @Override
    public String toString() {
        return "King";
    }

    @Override
    // Return all possible moves for the King piece
    public ArrayList<Position> possibleMoves(ArrayList<ArrayList<Piece>> board, Position pos) {
        ArrayList<Position> possibleMoves = new ArrayList<>();
        int row = pos.row;
        int col = pos.col;

        // Defines all possible move for king
        int[][] kingMoves = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1},  {1, 0},  {1, 1},
        };

        for(int[] move : kingMoves){
            int newRow = row + move[0];
            int newCol = col + move[1];

            // Checks if possible move has an occupied space and is in bounds
            if (isInBounds(newRow, newCol)) {
                Piece target = board.get(newRow).get(newCol);
                if(target == null || target.isWhite != this.isWhite) possibleMoves.add(new Position(newRow, newCol));
            }
        }
        return possibleMoves;
    }
}
