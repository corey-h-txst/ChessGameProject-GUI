package View;

import Controller.GameController;
import Controller.MoveHandler;
import Model.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class GUI extends JFrame {
    private JButton[][] boardButtons = new JButton[8][8];
    private JPanel boardPanel;
    private JPanel sidePanel;
    private JScrollPane logPanel;
    public JTextArea logText;
    public MoveHandler moveHandler;
    public GameController game;
    private Map<String, ImageIcon> pieceIcons = new HashMap<String, ImageIcon>();

    // Default constructor
    public GUI() {
        moveHandler = new MoveHandler(this);
        game = new GameController(this, moveHandler);

        setTitle("Java Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        boardPanel = createBoardPanel();
        sidePanel = createSidePanel();
        logPanel = createLogPanel();

        add(boardPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);
        add(logPanel, BorderLayout.SOUTH);
        loadPieceIcons();
        updateBoardGUI();

        setVisible(true);
    }

    // Creates the board part of the GUI
    private JPanel createBoardPanel() {
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));

        // Initializes board buttons and sets checkerboard pattern
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
    // Creates side panel which include New, Load, and Save Game as well as Undo Move
    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel(new GridLayout(4, 1));

        JButton newButton = new JButton("New Game");
        JButton loadButton = new JButton("Load Game");
        JButton saveButton = new JButton("Save Game");
        JButton undoButton = new JButton("Undo Move");

        // Defines New Game button logic
        newButton.addActionListener(e -> game.newGame());
        loadButton.addActionListener(e -> {
            // Opens file choose to select a save file to load
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("SaveFiles"));
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            // Once a file is chosen load it using GameController
            int result = chooser.showOpenDialog(this);
            if(result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                try {
                    game.loadGame(selectedFile.getName());
                    updateBoardGUI();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        // Defines Save Game button logic
        saveButton.addActionListener(e -> {
            // Creates popup for entering save file name
            JDialog dialog = new JDialog(this, "Save Game", true);
            dialog.setSize(300, 100);
            dialog.setLocationRelativeTo(this);

            JLabel label = new JLabel("Enter Save File Name:");
            dialog.add(label, BorderLayout.NORTH);

            JTextField text = new JTextField();
            dialog.add(text, BorderLayout.CENTER);

            // Final variable for lambda function
            final String[] fileName = {null};

            // Stores file name and closes popup
            text.addActionListener(e1 -> {
                fileName[0] = text.getText().trim();
                dialog.dispose();
            });

            dialog.setVisible(true);

            // Saves file under file name using GameController
            try {
                game.saveGame(fileName[0]);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        // Defines Undo Move button logic
        undoButton.addActionListener(e -> {
            // Removes last move from log, undoes board to last move made, and switches turn
            game.gameLog.removeLastMove();
            updateBoardGUI();
            moveHandler.isWhiteTurn = !moveHandler.isWhiteTurn;
            removeLastLogMove();
        });

        // Adds all buttons to side panel
        sidePanel.add(newButton);
        sidePanel.add(loadButton);
        sidePanel.add(saveButton);
        sidePanel.add(undoButton);

        return sidePanel;
    }
    // Creates log panel which outputs all moves made
    private JScrollPane createLogPanel() {
        logText = new JTextArea();
        logText.setEditable(false);
        JScrollPane logPane = new JScrollPane(logText);
        logPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        logPane.setPreferredSize(new Dimension(800, 125));
        return logPane;
    }
    // Helper function that outputs string to log panel text area
    public void outputLog(String text) {
        logText.append(text);
    }
    // Loads all piece icons from Resources folder
    private void loadPieceIcons() {
        String[] colors = {"white", "black"};
        String[] pieces = {"pawn", "knight", "bishop", "rook", "queen", "king"};

        // Loads each icon from directory
        for(String color : colors) {
            for(String piece : pieces) {
                String path = "Resources/" + color + "_" + piece + ".png";
                pieceIcons.put(color + "_" + piece, new ImageIcon(path));
            }
        }
    }
    // Update board GUI to current board state
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
    // Removes the last move entry from the log panel
    public void removeLastLogMove(){
        // Gets all moves from internal log and sets resets log text
        String text = logText.getText();
        logText.setText("");
        if(text.isEmpty()){ return;}

        // Splits the full internal log by newline
        String[] lines = text.split("\n");
        if(lines.length == 0){ return;}

        // Outputs all moves from internal logs except the last move
        for(int i = 0; i < lines.length - 1; i++){
            logText.append(lines[i] + "\n");
        }
    }
    // Creates popup for checkmate when the game ends
    public void checkmatePopup(String winner, JFrame mainFrame) {
        JOptionPane.showMessageDialog(mainFrame, "Checkmate! " + winner + " wins!");
        mainFrame.dispose();
    }
}
