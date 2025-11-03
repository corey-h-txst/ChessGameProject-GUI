package View;

import Controller.MoveHandler;
import Model.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class GUI extends JFrame {
    private JButton[][] boardButtons = new JButton[8][8];
    private JPanel boardPanel;
    private JPanel sidePanel;
    public  MoveHandler moveHandler;
    private Map<String, ImageIcon> pieceIcons = new HashMap<String, ImageIcon>();
    private Position highlightedPos;

    public GUI() {
        moveHandler = new MoveHandler(this);

        setTitle("Java Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        boardPanel = createBoardPanel();
        sidePanel = createSidePanel();

        add(boardPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);
        loadPieceIcons();
        updateBoardGUI();

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
                    button.setBackground(Color.DARK_GRAY);
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

    private void loadPieceIcons() {
        String[] colors = {"white", "black"};
        String[] pieces = {"pawn", "knight", "bishop", "rook", "queen", "king"};

        for(String color : colors) {
            for(String piece : pieces) {
                String path = "Resources/" + color + "_" + piece + ".png";
                pieceIcons.put(color + "_" + piece, new ImageIcon(path));
            }
        }
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
                    String key = (piece.isWhite ? "white_" : "black_") + piece.getClass().getSimpleName().toLowerCase();
                    ImageIcon icon = pieceIcons.get(key);
                    button.setIcon(icon);
                }
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }
}
