package com.findselfback.View;

import com.findselfback.Utilz.PrintColor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * GameView is a default window of The Find Self Back
 */
public class GameFrame extends JFrame {
    private GamePlayPanel gamePlayPanel = new GamePlayPanel(this);
    public GameFrame(){
        Image file16 = Toolkit.getDefaultToolkit().getImage("logo.png");

        PrintColor.debug(PrintColor.YELLOW,"GameFrame","Constructor","Start GameView...");
        setIconImage(file16);
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Find Self Back");


        add(gamePlayPanel);
        pack(); //Auto resize the window
        setLocationRelativeTo(null); //move the window to center screen
        setVisible(true);

    }
}
