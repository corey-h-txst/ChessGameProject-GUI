package Model;

import java.util.ArrayList;

public class Knight extends Piece{

    public  Knight(boolean isWhite){
        super(isWhite);
    }

    @Override
    public String toString() {
        return "Knight";
    }

    @Override
    // Returns all possible moves for the Knight piece
    public ArrayList<Position> possibleMoves(ArrayList<ArrayList<Piece>> board, Position pos) {
        ArrayList<Position> possibleMoves = new ArrayList<>();
        int row = pos.row;
        int col = pos.col;

        // Defines all possible knight moves
        int[][] knightMoves = {{-2, -1}, {-2, 1}, {2, -1}, {2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}};

        for(int[] move : knightMoves) {
            int newRow = row + move[0];
            int newCol = col + move[1];

            // Checks if possible move is in bounds and if it is occupied
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
