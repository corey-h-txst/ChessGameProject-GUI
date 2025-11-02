package Controller;

import Model.*;
import View.GUI;

import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MoveHandler{
    private boolean isWhiteTurn;
    private Position selectedPos;
    private final boolean white = true;
    private final boolean black = false;
    private final ArrayList<ArrayList<Piece>> board;
    private GUI gui;

    public MoveHandler(GUI gui){
        this.gui = gui;
        this.board = new ArrayList<ArrayList<Piece>>();

        ArrayList<Piece> currRow = new ArrayList<>(8);

        // Creates king and pawn rows for black
        board.add(createKingRow(black));
        board.add(createPawnRow(black));
        // Adds empty rows
        for(int i = 0; i < 6; i++){
            board.add(createEmptyRow());
        }
        // Creates king and pawn rows for white
        board.add(createPawnRow(white));
        board.add(createKingRow(white));
    }

    private ArrayList<Piece> createPawnRow(boolean color){
        ArrayList<Piece> currRow = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            currRow.add(new Pawn(color));
        }
        return currRow;
    }
    private ArrayList<Piece> createKingRow(boolean color){
        ArrayList<Piece> currRow = new ArrayList<>(8);
        currRow.add(new Rook(color));
        currRow.add(new Knight(color));
        currRow.add(new Bishop(color));
        currRow.add(new Queen(color));
        currRow.add(new King(color));
        currRow.add(new Bishop(color));
        currRow.add(new Knight(color));
        currRow.add(new Rook(color));
        return currRow;
    }
    private ArrayList<Piece> createEmptyRow(){
        ArrayList<Piece> currRow = new ArrayList<>(8);
        for(int i = 0; i < 8; i++){
            currRow.add(null);
        }
        return currRow;
    }

    // Handles both first and second click determining validity and executing the move
    public void handleClick(int row, int col){
        Position clicked = new Position(row, col);

        // Gets selectedPos from handleClick's arguments
        if (selectedPos == null) {
            Piece piece = getPieceAt(clicked);
            // Checks if a piece is at position
            if(piece == null){
                return;
            }
            // Checks if piece is of same color as current player turn
            if(piece.isWhite != isWhiteTurn){
                return;
            }
            // If clicked position is valid set selectedPos to clicked
            selectedPos = clicked;
        }
        else {
            if(testValidMove(selectedPos, clicked)) {
                movePiece(selectedPos, clicked);
                isWhiteTurn = !isWhiteTurn;
            }
            selectedPos = null;
        }
    }

    // Return the piece at any given position
    private Piece getPieceAt(Position pos){
        if(board.get(pos.row).get(pos.col) == null){}
        return null;
    }

    // Moves piece to target and sets original position to null
    private void movePiece(Position from, Position to){
        Piece piece = getPieceAt(from);
        board.get(from.row).set(from.col, null);
        board.get(to.row).set(to.col, piece);

    }
    // Tests if move is legal (doesn't lead to checkmate)
    boolean testValidMove(Position curr, Position next) {
        Piece piece = getPieceAt(curr);

        ArrayList<Position> legalMoves = new ArrayList<>();
        for (Position move : piece.possibleMoves(board)) {
            Piece captured = getPieceAt(move);

            movePiece(curr, move);

            if (!isCheck(piece.isWhite)) {
                legalMoves.add(move);
            }

            board.get(curr.row).set(curr.col, piece);
            board.get(move.row).set(move.col, captured);
        }

        if(legalMoves.contains(next)) return true;
        return false;
    }

    public boolean isCheck(Color color) {
        // Obtains location of king
        Position kingSquare = null;
        for (ArrayList<Square> row : board) {
            for (Square square : row) {
                if (!square.piece.isEmpty()) {
                    Piece piece = square.piece.getFirst();
                    if (piece instanceof King && piece.color == color) {
                        kingSquare = piece.position;
                        break;
                    }
                }
            }
        }

        // Determines if king is under attack by an enemy piece
        for (ArrayList<Square> row : board) {
            for (Square square : row) {
                if (!square.piece.isEmpty()) {
                    Piece attack = square.piece.getFirst();
                    if (attack.color != color) {
                        ArrayList<Position> moves = attack.possibleMoves(this);
                        if (moves.contains(kingSquare)) return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(Color color) {
        if (!isCheck(color)) {
            return false;
        }

        // Tests if any friendly pieces (including king) can be moved to avoid checkmate
        for (ArrayList<Square> row : board) {
            for (Square square : row) {
                if (!square.piece.isEmpty()) {
                    Piece piece = square.piece.getFirst();
                    if (piece.color == color) {

                        ArrayList<Position> moves = piece.possibleMoves(this);
                        for (Position move : moves) {
                            Piece captured = getPiece(move);
                            Position originalPos = piece.position;

                            movePiece(originalPos, move);

                            if (!isCheck(color)) {
                                board.get(originalPos.row).get(originalPos.col).piece.add(piece);
                                piece.position = originalPos;
                                board.get(move.row).get(move.col).piece.clear();
                                if (captured != null) board.get(move.row).get(move.col).piece.add(captured);

                                System.out.println("Check!");
                                return false;
                            }

                            board.get(originalPos.row).get(originalPos.col).piece.add(piece);
                            piece.position = originalPos;
                            board.get(move.row).get(move.col).piece.clear();
                            if (captured != null) board.get(move.row).get(move.col).piece.add(captured);
                        }
                    }
                }
            }
        }
        System.out.println("Checkmate!");
        return true;
    }
}