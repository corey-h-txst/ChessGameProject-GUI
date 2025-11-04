package Controller;

import Model.*;
import View.GUI;

import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MoveHandler{
    private GUI gui;
    private boolean isWhiteTurn = true;
    private Position selectedPos = null;
    private final boolean white = true;
    private final boolean black = false;
    private final ArrayList<ArrayList<Piece>> board;

    public MoveHandler(GUI gui){
        this.gui = gui;
        this.board = new ArrayList<>();

        // Creates king and pawn rows for black
        board.add(createKingRow(black));
        board.add(createPawnRow(black));
        // Adds empty rows
        for(int i = 0; i < 4; i++){
            board.add(createEmptyRow());
        }
        // Creates king and pawn rows for white
        board.add(createPawnRow(white));
        board.add(createKingRow(white));
    }

    public void resetBoard(){
        board.set(0, createKingRow(black));
        board.set(1, createPawnRow(black));
        for(int i = 2; i < 6; i++){
            board.set(0, createEmptyRow());
        }
        board.set(6, createPawnRow(white));
        board.set(7, createKingRow(white));
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
            // Checks if a piece is at position or if piece is player's color and return if not
            if(piece == null || piece.isWhite != isWhiteTurn){
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
            gui.updateBoardGUI();
        }
    }

    // Return the piece at any given position
    public Piece getPieceAt(Position pos){
        if(board.get(pos.row).get(pos.col) != null){return board.get(pos.row).get(pos.col);}
        return null;
    }

    // Moves piece to target and sets original position to null
    public void movePiece(Position from, Position to){
        Piece piece = getPieceAt(from);
        board.get(from.row).set(from.col, null);
        board.get(to.row).set(to.col, piece);

    }
    // Tests if move is legal (doesn't lead to checkmate)
    boolean testValidMove(Position curr, Position next) {
        Piece piece = getPieceAt(curr);

        ArrayList<Position> legalMoves = new ArrayList<>();
        for (Position move : piece.possibleMoves(board, curr)) {
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

    public boolean isCheck(boolean isWhite) {
        Position kingSquare = null;
        // Finds location of king
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                Piece piece = board.get(row).get(col);
                if(piece instanceof King && piece.isWhite == isWhite){
                    kingSquare = new Position(row, col);
                    break;
                }
            }
        }

        // Tests if any enemy pieces can attack king
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                Piece piece = board.get(row).get(col);
                if(piece != null && piece.isWhite != isWhite){
                    ArrayList<Position> moves = piece.possibleMoves(board, new Position(row, col));
                    for(Position move : moves){
                        if(move.equals(kingSquare)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(boolean isWhite) {
        if (!isCheck(isWhite)) {
            return false;
        }

        // Tests if any friendly pieces (including king) can be moved to avoid checkmate
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                Piece piece = board.get(row).get(col);
                if(piece != null && piece.isWhite != isWhite){
                    ArrayList<Position> moves = piece.possibleMoves(board, new Position(row, col));
                    for(Position move : moves){
                        // Store current board state
                        Piece captured = getPieceAt(move);

                        // Make move
                        movePiece(new Position(row, col), move);

                        // Check if king is still in check
                        boolean stillCheck = isCheck(isWhite);

                        // Undo move
                        board.get(row).set(col, piece);
                        board.get(move.row).set(move.col, captured);

                        if(!stillCheck){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}