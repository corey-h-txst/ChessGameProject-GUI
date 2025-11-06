package View;

import Controller.GameController;
import Controller.MoveHandler;
import Model.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

        JButton newButton = new JButton("New Game");
        JButton loadButton = new JButton("Load Game");
        JButton saveButton = new JButton("Save Game");
        JButton undoButton = new JButton("Undo Move");

        newButton.addActionListener(e -> game.newGame());
        loadButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("SaveFiles"));
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

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
        saveButton.addActionListener(e -> {
            JDialog dialog = new JDialog(this, "Save Game", true);
            dialog.setSize(300, 100);
            dialog.setLocationRelativeTo(this);

            JLabel label = new JLabel("Enter Save File Name:");
            dialog.add(label, BorderLayout.NORTH);

            JTextField text = new JTextField();
            dialog.add(text, BorderLayout.CENTER);

            final String[] fileName = {null};

            text.addActionListener(e2 -> {
                fileName[0] = text.getText().trim();
                dialog.dispose();
            });

            dialog.setVisible(true);

            try {
                game.saveGame(fileName[0]);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        undoButton.addActionListener(e -> {
            game.gameLog.removeLastMove();
            updateBoardGUI();
            moveHandler.isWhiteTurn = !moveHandler.isWhiteTurn;
            removeLastLogMove();
        });

        sidePanel.add(newButton);
        sidePanel.add(loadButton);
        sidePanel.add(saveButton);
        sidePanel.add(undoButton);

        return sidePanel;
    }

    private JScrollPane createLogPanel() {
        logText = new JTextArea();
        logText.setEditable(false);
        JScrollPane logPane = new JScrollPane(logText);
        logPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        logPane.setPreferredSize(new Dimension(800, 125));
        return logPane;
    }

    public void outputLog(String text) {
        logText.append(text);
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

    public void removeLastLogMove(){
        String text = logText.getText();
        logText.setText("");
        if(text.isEmpty()){ return;}

        String[] lines = text.split("\n");
        if(lines.length == 0){ return;}

        for(int i = 0; i < lines.length - 1; i++){
            logText.append(lines[i] + "\n");
        }
    }

    public void checkmatePopup(String winner, JFrame mainFrame) {
        JOptionPane.showMessageDialog(mainFrame, "Checkmate! " + winner + " wins!");
        mainFrame.dispose();
    }
}
