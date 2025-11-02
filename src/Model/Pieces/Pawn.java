package Pieces;

import Game.Board;
import UtilityClasses.*;

import java.util.ArrayList;

public class Pawn extends Piece{
    public final char symbol = 'P';

    public Pawn(Color color, Position position){
        super(color, position);
    }

    @Override
    public char getSymbol(){
        return symbol;
    }

    @Override
    public ArrayList<Position> possibleMoves(Board board){
        ArrayList<Position> possibleMoves = new ArrayList<>();
        // Determines direction of pawn (white upwards, black downwards)
        int direction = (color.color == 'W') ? -1 : 1;

        Position forward = new Position(position.row + direction, position.col);
        if (board.isValidPosition(forward) && board.getPiece(forward) == null) {
            possibleMoves.add(forward);

            // Handles two square movement on a pawn's first move
            int startingRow = (color.color == 'W') ? 6 : 1;
            if (position.row == startingRow) {
                Position doubleForward = new Position(position.row + 2 * direction, position.col);
                if(board.isValidPosition(doubleForward) && board.getPiece(doubleForward) == null){
                possibleMoves.add(doubleForward);
                }
            }
        }
        // Handles logic diagonal movement which requires the pawn to take a piece
        Position takeLeft = new Position(position.row + direction, position.col - 1);
        if (board.isValidPosition(takeLeft)){
            Piece target = board.getPiece(takeLeft);
            if (target != null && target.color != color) possibleMoves.add(takeLeft);
        }
        Position takeRight = new Position(position.row + direction, position.col - 1);
        if (board.isValidPosition(takeRight)) {
            Piece target = board.getPiece(takeRight);
            if (target != null && target.color != color) possibleMoves.add(takeRight);
        }

        return possibleMoves;
    }
}
