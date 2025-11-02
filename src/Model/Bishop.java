package Model;

import java.util.ArrayList;

public class Bishop extends Piece {

    public Bishop(boolean isWhite){super(isWhite);}

    @Override
    public ArrayList<Position> possibleMoves(ArrayList<ArrayList<Piece>> board, Position pos) {
        ArrayList<Position> possibleMoves = new ArrayList<>();
        int row = pos.row;
        int col = pos.col;

        // Defines diagonal movement for bishop
        int[][] bishopMoves = {{-1, -1}, {1, -1}, {-1, 1}, {1, 1}};

        for (int[] direction : bishopMoves) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            while(isInBounds(newRow, newCol)) {
                Piece target = board.get(newRow).get(newCol);

                // Empty square check
                if(target == null){
                    possibleMoves.add(new Position(newRow, newCol));
                }
                // Occupied square check
                else{
                    if(target.isWhite != this.isWhite){
                        possibleMoves.add(new Position(newRow,newCol));
                    }
                    break;
                }
                // Extends diagonal movement if not occupied square or out of bounds
                newRow += direction[0];
                newCol += direction[1];
            }
        }

        return possibleMoves;
    }
}
