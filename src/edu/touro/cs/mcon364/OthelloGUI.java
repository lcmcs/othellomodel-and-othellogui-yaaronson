package edu.touro.cs.mcon364;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class OthelloGUI extends JFrame {
    private JButtonWithPoint[][] buttonGrid;
    private OthelloModel model1 = new OthelloModel();
    private PlayerValue color;
    private int showValidMoves;

    public <Othello> OthelloGUI(Othello model){
        PlayerValue[][] othelloGrid = model1.toArray();
        buttonGrid = new JButtonWithPoint[othelloGrid.length][othelloGrid.length];
        color = PlayerValue.BLACK;
        OthelloGUI instance = this;
        setTitle("Othello Game");
        super.setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new FlowLayout());
        JLabel statusBar = new JLabel();
        statusBar.setText(PlayerValue.BLACK.name());
        statusPanel.add(statusBar);
        JPanel game = new JPanel();
        game.setLayout(new GridLayout(model1.getBoardSize(), model1.getBoardSize()));

        add(game);
        add(statusPanel, BorderLayout.SOUTH);

        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JButtonWithPoint b = (JButtonWithPoint) actionEvent.getSource();
                boolean move = model1.makeMove(new OthelloModel.Cell(b.x, b.y), color);
                if(!move){
                    JOptionPane.showMessageDialog(instance, "Error, try again");
                    return;
                }
                else{
                    color = color.opposite();
                    statusBar.setText(color.name());
                }
                for(int row = 0; row < buttonGrid.length; row++){
                    for (int col = 0; col < buttonGrid[row].length; col++){
                        buttonGrid[row][col].setText(model1.toArray()[row][col].toGUI());
                    }
                }
                if(model1.isGameOver()){
                    JOptionPane.showMessageDialog(instance, "Game over. " + model1.getWinner().name() + " wins!");
                    setVisible(false);
                    dispose();
                }
                else if(!model1.validMoveExists(color)){
                    JOptionPane.showMessageDialog(instance, color.name() + " has no valid move. " + color.opposite().name() + " it's your turn.");
                    color = color.opposite();
                    statusBar.setText(color.name());
                }
                else {
                    model1.computerMove();
                    color = color.opposite();
                    for(int row = 0; row < buttonGrid.length; row++) {
                        for (int col = 0; col < buttonGrid[row].length; col++) {
                            buttonGrid[row][col].setText(model1.toArray()[row][col].toGUI());

                        }
                    }
                }
            }
        };

        for(int row = 0; row < othelloGrid.length; row++){
            for(int col = 0; col < othelloGrid[row].length; col++) {
                JButtonWithPoint button = new JButtonWithPoint();
                button.setText(othelloGrid[row][col].toGUI());
                button.x = row;
                button.y = col;
                game.add(button);
                buttonGrid[row][col] = button;
                button.addActionListener(al);
            }
        }
        this.setVisible(true);

    }

    private static class JButtonWithPoint extends JButton{
        int x, y;
    }
}


