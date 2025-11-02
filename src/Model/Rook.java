package Model;

import java.util.ArrayList;

public class Rook extends Piece{

    public Rook(boolean isWhite) {super(isWhite);}

    @Override
    public ArrayList<Position> possibleMoves(ArrayList<ArrayList<Piece>> board, Position pos) {
        ArrayList<Position> possibleMoves = new ArrayList<>();
        int row = pos.row;
        int col = pos.col;

        // Defines up, down, left, and right movement
        int[][] rookMoves = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for(int[] direction : rookMoves){
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            while (isInBounds(newRow, newCol)) {
                Piece target = board.get(newRow).get(newCol);
                // Empty square check
                if (target == null){
                    possibleMoves.add(new Position(newRow,newCol));
                }
                // Occupied square check
                else{
                    if(target.isWhite != this.isWhite){
                        possibleMoves.add(new Position(newRow,newCol));
                    }
                    break;
                }
                // Extend movement in direction if square is not occupied or out of bounds
                newRow = newRow + direction[0];
                newCol = newCol + direction[1];
            }
        }

        return possibleMoves;
    }
}
