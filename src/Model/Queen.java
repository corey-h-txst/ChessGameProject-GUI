package Model;

import java.util.ArrayList;

public class Queen extends Piece{

    public Queen(boolean isWhite ) {super(isWhite);}

    @Override
    public ArrayList<Position> possibleMoves(ArrayList<ArrayList<Piece>> board, Position pos){
        ArrayList<Position> possibleMoves = new ArrayList<>();
        int row = pos.row;
        int col = pos.col;

        // Defines all possible movement of queen
        int[][] queenMoves = {
                {-1, 0}, {1, 0},
                {0, -1}, {0, 1},
                {-1, -1}, {-1, 1},
                {1, -1}, {1, 1},
        };

        for(int[] direction : queenMoves){
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            while (isInBounds(newRow, newCol)) {
                Piece target = board.get(newRow).get(newCol);

                // Empty square check
                if(target == null){
                    possibleMoves.add(new Position(newRow,newCol));
                }
                // Occupied square check
                else{
                    if(target.isWhite != isWhite){
                        possibleMoves.add(new Position(newRow,newCol));
                    }
                    break;
                }
                // Extends movement in current direction if not occupied or out of bounds
                newRow += direction[0];
                newCol += direction[1];
            }
        }

        return possibleMoves;
    }

}
