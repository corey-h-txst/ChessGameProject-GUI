package Pieces;

import Game.Board;
import UtilityClasses.*;

import java.util.ArrayList;

public class Bishop extends Piece {
    public final char symbol = 'B';

    public Bishop(Color color, Position position){
        super(color, position);
    }

    @Override
    public char getSymbol(){
        return symbol;
    }

    @Override
    public ArrayList<Position> possibleMoves(Board board) {
        ArrayList<Position> possibleMoves = new ArrayList<>();
        int[][] bishopMoves = {{-1, -1}, {1, -1}, {-1, 1}, {1, 1}};
        Position possible = new Position(0, 0);

        for (int[] move : bishopMoves) {
            int row = position.row + move[0];
            int col = position.col + move[1];

            while (true){
                possible.row = row;
                possible.col = col;
                if(!board.isValidPosition(possible)) break;

                Piece target =  board.getPiece(possible);
                if (target != null){
                    if (target.getColor() != color.color) possibleMoves.add(new Position(row, col));
                    break;
                }
                possibleMoves.add(new Position(row, col));
                row += move[0];
                col += move[1];
            }
        }

        return possibleMoves;
    }
}
