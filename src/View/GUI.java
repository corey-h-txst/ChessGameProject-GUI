package View;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUI extends JFrame {
    private JButton[][] boardButtons = new JButton[8][8];
    private JPanel boardPanel;
    private JPanel sidePanel;

    public GUI() {
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

                if((row + col) % 2 == 0) {
                    button.setBackground(Color.WHITE);
                }
                else {
                    button.setBackground(Color.BLACK);
                }

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
    
}
