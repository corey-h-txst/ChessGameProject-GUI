package View;

import Controller.MoveHandler;
import Model.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUI extends JFrame {
    private JButton[][] boardButtons = new JButton[8][8];
    private JPanel boardPanel;
    private JPanel sidePanel;
    private MoveHandler moveHandler;

    public GUI() {
        moveHandler = new MoveHandler();

        setTitle("Java Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        boardPanel = createBoardPanel();
        sidePanel = createSidePanel();

        add(boardPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);

        setVisible(true);
    }

    private JPanel createBoardPanel() {
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton button = new JButton();
                boardButtons[row][col] = button;

                // Set square color
                if((row + col) % 2 == 0) {
                    button.setBackground(Color.WHITE);
                }
                else {
                    button.setBackground(Color.BLACK);
                }

                // Stored for lambda expression
                int r = row;
                int c = col;

                // Sends location of clicked board square to moveHandler
                button.addActionListener(e -> moveHandler.handleClick(r, c));

                boardPanel.add(button);
            }
        }
        return boardPanel;
    }
    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel(new GridLayout(4, 1));

        JButton settingsButton = new JButton("Settings");
        JButton startButton = new JButton("New Game");
        JButton loadButton = new JButton("Load Game");
        JButton saveButton = new JButton("Save Game");

        sidePanel.add(settingsButton);
        sidePanel.add(startButton);
        sidePanel.add(loadButton);
        sidePanel.add(saveButton);

        return sidePanel;
    }

    public void updateBoardGUI(){
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton button = boardButtons[row][col];
                Piece piece = moveHandler.getPieceAt(new Position(row, col));

                if(piece == null){
                    button.setIcon(null);
                }
                else{
                    String symbol = "";
                    if(piece instanceof Pawn) symbol = piece.isWhite ? "\u2659" : "\u265F";
                    else if(piece instanceof Knight) symbol = piece.isWhite ? "\u2656" : "\u265C";
                    else if(piece instanceof Rook) symbol = piece.isWhite ? "\u2658" : "\u265E";
                    else if(piece instanceof Bishop) symbol = piece.isWhite ? "\u2657" : "\u265D";
                    else if(piece instanceof Queen) symbol = piece.isWhite ? "\u2655" : "\u265B";
                    else if(piece instanceof King) symbol = piece.isWhite ? "\u2654" : "\u265A";
                }
            }
        }
    }
    
}
