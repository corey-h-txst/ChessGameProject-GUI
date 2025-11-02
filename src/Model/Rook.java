package Model;

import java.util.ArrayList;

public class Rook extends Piece{

    public Rook(boolean isWhite) {super(isWhite);}

    @Override
    public ArrayList<Position> possibleMoves(Board board){
        ArrayList<Position> possibleMoves = new ArrayList<>();
        Position possible = new Position(position.row, position.col);

        // Check move down
        while (true){
            possible.row++;
            if (!board.isValidPosition(possible)) break;
            if (board.getPiece(possible) != null){
                if (board.getPiece(possible).getColor() != color.color) possibleMoves.add(new Position(possible.row, possible.col));
                break;
            }
            possibleMoves.add(new Position(possible.row, possible.col));
        }
        possible.row = this.position.row;
        possible.col = this.position.col;

        // Check move right
        while (true){
            possible.col++;
            if(!board.isValidPosition(possible)) break;
            if (board.getPiece(possible) != null){
                if (board.getPiece(possible).getColor() != color.color) possibleMoves.add(new Position(possible.row, possible.col));
                break;
            }
            possibleMoves.add(new Position(possible.row, possible.col));
        }
        possible.row = this.position.row;
        possible.col = this.position.col;

        // Check move up
        while (true){
            possible.row--;
            if(!board.isValidPosition(possible)) break;
            if (board.getPiece(possible) != null){
                if (board.getPiece(possible).getColor() != color.color)possibleMoves.add(new Position(possible.row, possible.col));
                break;
            }
            possibleMoves.add(new Position(possible.row, possible.col));
        }
        possible.row = this.position.row;
        possible.col = this.position.col;

        // Check move left
        while (true){
            possible.col--;
            if(!board.isValidPosition(possible)) break;
            if (board.getPiece(possible) != null){
                if (board.getPiece(possible).getColor() != color.color)possibleMoves.add(new Position(possible.row, possible.col));
                break;
            }
            possibleMoves.add(new Position(possible.row, possible.col));
        }

        return possibleMoves;
    }
}
