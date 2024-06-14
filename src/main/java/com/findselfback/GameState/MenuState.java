package com.findselfback.GameState;

import com.findselfback.Control.Statemethod;
import com.findselfback.UI.MenuButton;
import com.findselfback.Utilz.Constant;
import com.findselfback.Utilz.PrintColor;
import com.findselfback.View.GamePlayPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

import static com.findselfback.Utilz.Constant.UI.MenuSize.FVF_FERNANDO;

public class MenuState extends State implements Statemethod {
    private MenuButton startButton;
    private Image icon;
    public MenuState(GamePlayPanel gamePlayPanel) {
        super(gamePlayPanel);
        init();
    }
    private void init(){
        icon = new ImageIcon("Resources/background/MenuBG2.gif").getImage();
        startButton = new MenuButton(GamePlayPanel.SCREEN_WIDTH/2,(GamePlayPanel.SCREEN_HEIGHT- Constant.UI.MenuSize.SCALING_WIDTH)/2,"Start");
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(icon, 0, 0,GamePlayPanel.SCREEN_WIDTH,GamePlayPanel.SCREEN_HEIGHT, null);
        g.setColor(Color.WHITE);
        g.setFont(FVF_FERNANDO);
//        g.drawString("Start",GamePlayPanel.SCREEN_WIDTH/7, GamePlayPanel.SCREEN_HEIGHT/2);
        startButton.draw(g);
        g.setColor(Color.black);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(startButton.isIn(e)){
            startButton.setStateColor(startButton.PRESSED);
        } else {
            startButton.setStateColor(startButton.NO_ON);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(startButton.isIn(e)){
            startButton.setStateColor(startButton.ON);
            GameState.state = GameState.PLAYING;
            PrintColor.debug(PrintColor.GREEN_BACKGROUND,"MenuState","mouseReleased","Game start");
            gamePlayPanel.getPlaying().getAudioPlayer().getEnvironmentAudioList()[0].play();
        } else {
            startButton.setStateColor(startButton.NO_ON);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(startButton.isIn(e)){
            startButton.setStateColor(startButton.ON);
        } else {
            startButton.setStateColor(startButton.NO_ON);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
