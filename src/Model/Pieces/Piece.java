package Pieces;

import UtilityClasses.*;
import Game.Board;

import java.util.ArrayList;

public abstract class Piece {
    public Color color;
    public Position position;
    public char symbol = ' ';

    public Piece(Color color, Position position) {
        this.color = color;
        this.position = position;
    }

    // Helper function for easy access of the char connected to the Color class
    public char getColor(){
        return color.color;
    }
    // Overridden in all subclasses so that proper symbol is accessed when printing board state
    public char getSymbol(){
        return symbol;
    }
    // Default possibleMoves function which is overridden in all subclasses with piece-specific behavior
    public ArrayList<Position> possibleMoves(Board board){
        return new ArrayList<Position>();
    }
}
