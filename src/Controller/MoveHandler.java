package Controller;

import Model.*;
import View.GUI;

import java.util.ArrayList;

public class MoveHandler{
    private GUI gui;
    public boolean isWhiteTurn = true;
    private Position selectedPos = null;
    private final boolean white = true;
    private final boolean black = false;
    private final ArrayList<ArrayList<Piece>> board;

    // Default constructor
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
    // Sets current board to a blank new board
    public void resetBoard(){
        board.set(0, createKingRow(black));
        board.set(1, createPawnRow(black));
        for(int i = 2; i < 6; i++){
            board.set(i, createEmptyRow());
        }
        board.set(6, createPawnRow(white));
        board.set(7, createKingRow(white));
    }
    // Helper function used to create a row of pawns of a given color
    private ArrayList<Piece> createPawnRow(boolean color){
        ArrayList<Piece> currRow = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            currRow.add(new Pawn(color));
        }
        return currRow;
    }
    // Helper function used to create a king row of a given color
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
    // Helper function that creates a row with no pieces
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

        // Checks if a piece to be moved has already been selected
        if (selectedPos == null) {
            Piece piece = getPieceAt(clicked);
            // Checks if a piece is at position or if piece is player's color and return if not
            if(piece == null || piece.isWhite != isWhiteTurn){
                return;
            }
            // If clicked position is valid set selectedPos to clicked
            selectedPos = clicked;
        }
        // Moves piece to clicked position if a piece to be moved has been selected
        else {
            if(testValidMove(selectedPos, clicked)) {
                // Moves piece and switches turns
                movePiece(selectedPos, clicked, true);
                isWhiteTurn = !isWhiteTurn;
            }
            // Resets selectedPos and updates board in GUI
            selectedPos = null;
            gui.updateBoardGUI();
        }
        isCheckmate(isWhiteTurn);
    }
    // Helper function that returns the piece at any given position
    public Piece getPieceAt(Position pos){
        if(board.get(pos.row).get(pos.col) != null){return board.get(pos.row).get(pos.col);}
        return null;
    }
    // Helper function that sets piece to given position (used by undo move to restore pieces)
    public void setPieceAt(Position pos, Piece piece){
        board.get(pos.row).set(pos.col, piece);
    }
    // Moves piece to target and sets original position to null
    public void movePiece(Position from, Position to, boolean moveLog){
        // Checks if move is meant to be displayed on board and stored in log
        if(moveLog) {
            // Checks if a piece is being taken
            if (getPieceAt(to) != null) {
                // Outputs move to move log GUI
                gui.logText.append(getPieceAt(from).toString() + " " + from.toString() + " takes " + getPieceAt(to).toString() + " " + to.toString() + "\n");
                // Adds move to internal move log
                String move = ("" + from.row + from.col + to.row + to.col);
                String takenPiece = (getPieceAt(to).isWhite) ? "w" : "b";
                takenPiece = (takenPiece + getPieceAt(to).toString().charAt(0)).toLowerCase();
                gui.game.gameLog.addMove(move + takenPiece);
            } else {
                // Outputs move to move log GUI
                gui.logText.append(getPieceAt(from).toString() + " " + from.toString() + " to " + to.toString() + "\n");
                // Adds move to internal move log
                String move = ("" + from.row + from.col + to.row + to.col);
                gui.game.gameLog.addMove(move);
            }
        }
        // Executes move
        Piece piece = getPieceAt(from);
        board.get(from.row).set(from.col, null);
        board.get(to.row).set(to.col, piece);

    }
    // Tests if move is legal (doesn't lead to checkmate)
    boolean testValidMove(Position curr, Position next) {
        Piece piece = getPieceAt(curr);
        ArrayList<Position> legalMoves = new ArrayList<>();

        // Creates a list of legal moves
        for (Position move : piece.possibleMoves(board, curr)) {
            Piece captured = getPieceAt(move);
            // Executes a test move
            movePiece(curr, move, false);
            // Checks if move leads to a self checkmate
            if (!isCheck(piece.isWhite)) {
                legalMoves.add(move);
            }
            // Resets test move
            board.get(curr.row).set(curr.col, piece);
            board.get(move.row).set(move.col, captured);
        }
        // Returns if move is legal or not
        if(legalMoves.contains(next)) return true;
        return false;
    }
    // Determines if a passed color is in check
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
    // Determines if a passed color is in checkmate
    public boolean isCheckmate(boolean isWhite) {
        if (!isCheck(isWhite)) {
            return false;
        }

        // Tests if any friendly pieces (including king) can be moved to avoid checkmate
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                Piece piece = board.get(row).get(col);
                if(piece != null && piece.isWhite == isWhite){
                    ArrayList<Position> moves = piece.possibleMoves(board, new Position(row, col));
                    for(Position move : moves){
                        // Store current board state
                        Piece captured = getPieceAt(move);

                        // Make move
                        movePiece(new Position(row, col), move, false);

                        // Check if king is still in check
                        boolean stillCheck = isCheck(isWhite);

                        // Undo move
                        board.get(row).set(col, piece);
                        board.get(move.row).set(move.col, captured);

                        // If a move exists that prevents checkmate return false
                        if(!stillCheck){
                            gui.logText.append("Check!\n");
                            return false;
                        }
                    }
                }
            }
        }
        // Color is in checkmate so create end game popup
        gui.logText.append("Checkmate!\n");
        String winOutput = (isWhiteTurn) ? "Black" : "White";
        gui.checkmatePopup(winOutput, gui);
        return true;
    }
}