package com.findselfback.View;

import com.findselfback.Utilz.PrintColor;

import javax.swing.*;

/**
 * GameView is a default window of The Find Self Back
 */
public class GameFrame extends JFrame {
    private GamePlayPanel gamePlayPanel = new GamePlayPanel(this);
    public GameFrame(){
        PrintColor.debug(PrintColor.YELLOW,"GameFrame","Constructor","Start GameView...");
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Find Self Back");

        add(gamePlayPanel);
        pack(); //Auto resize the window
        setLocationRelativeTo(null); //move the window to center screen
        setVisible(true);
    }
}
