package com.findselfback.GameState;

import com.findselfback.Control.Statemethod;
import com.findselfback.Entities.Player;
import com.findselfback.Level.LoadSave;
import com.findselfback.Level.MapManager;
import com.findselfback.View.GamePlayPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


public class Playing extends State implements Statemethod {
    private Player player;
    private MapManager mapManager;
    public boolean upPressed = false, rightPressed = false, downPressed = false, leftPressed = false;
    public boolean isNoPressed = true;
    public int lastPressed;

    public Playing(GamePlayPanel gamePlayPanel) {
        super(gamePlayPanel);
        init();
    }

    public void init(){
        player = new Player(this,"src/main/resources/assets/PlayerSheet2.png", gamePlayPanel.getInputHandle());

        mapManager = new MapManager(gamePlayPanel);
        mapManager.autoGetSprite(LoadSave.getSpriteAtlas(LoadSave.MAP_ATLAS_PATH));
        mapManager.setLevel(LoadSave.STAGE_ONE_PATH);
    }

    @Override
    public void update() {
        mapManager.update();
        player.update();
    }

    @Override
    public void draw(Graphics g) {
        mapManager.draw(g);
        player.paint(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

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
        int eventCode = e.getKeyCode();
        if(eventCode == KeyEvent.VK_W){ //W button
            upPressed = true;
        }
        if(eventCode == KeyEvent.VK_D){ //D button
            rightPressed = true;
        }
        if(eventCode == KeyEvent.VK_S){ //S button
            downPressed = true;
        }if(eventCode == KeyEvent.VK_A){ //A button
            leftPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int eventCode = e.getKeyCode();
        if(eventCode == KeyEvent.VK_W){ //W button
            upPressed = false;
        }
        if(eventCode == KeyEvent.VK_D){ //D button
            rightPressed = false;
        }
        if(eventCode == KeyEvent.VK_S){ //S button
            downPressed = false;
        }if(eventCode == KeyEvent.VK_A){ //A button
            leftPressed = false;
        }
        lastPressed = eventCode;
    }

    public boolean isNoPressed(){
        return !upPressed && !rightPressed && !downPressed && !leftPressed;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public Player getPlayer() {
        return player;
    }
}
