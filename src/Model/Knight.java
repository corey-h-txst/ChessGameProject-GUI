package Model;

import java.util.ArrayList;

public class Knight extends Piece{

    public  Knight(boolean isWhite){
        super(isWhite);
    }

    @Override
    public ArrayList<Position> possibleMoves(ArrayList<ArrayList<Piece>> board, Position pos) {
        ArrayList<Position> possibleMoves = new ArrayList<>();
        int row = pos.row;
        int col = pos.col;

        // Defines all possible knight moves
        int[][] knightMoves = {{-2, -1}, {-2, 1}, {2, -1}, {2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}};

        for(int[] move : knightMoves) {
            int newRow = row + move[0];
            int newCol = col + move[1];

            if (isInBounds(newRow, newCol)) {
                Piece target = board.get(newRow).get(newCol);
                if (target == null || target.isWhite != this.isWhite) {
                    possibleMoves.add(new Position(newRow, newCol));
                }
            }
        }

        return possibleMoves;
    }
}
