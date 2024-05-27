package com.findselfback.GameState;

import com.findselfback.Control.Statemethod;
import com.findselfback.Entities.Player;
import com.findselfback.Level.LoadSave;
import com.findselfback.Level.MapManager;
import com.findselfback.View.GamePlayPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Playing extends State implements Statemethod {
    private Player player;
    private MapManager mapManager;
    public boolean upPressed = false, rightPressed = false, downPressed = false, leftPressed = false;
    public boolean isNoPressed = true;
    public int lastPressed;
    private int xLevelOffset;
    private int leftBorder = (int)(0.2 * GamePlayPanel.SCREEN_WIDTH);
    private int rightBorder = (int)(0.8 * GamePlayPanel.SCREEN_WIDTH);
    private int totalTileWidth;
    private int maxTileWidth;
    private int maxOffsetWidth;
    private BufferedImage backgroundImage;


    public Playing(GamePlayPanel gamePlayPanel) {
        super(gamePlayPanel);
        init();
    }

    public void init(){
        player = new Player(this,"src/main/resources/assets/PlayerSheet2.png", gamePlayPanel.getInputHandle());
        mapManager = new MapManager(gamePlayPanel);
        mapManager.autoGetSprite(LoadSave.getSpriteAtlas(LoadSave.MAP_ATLAS_PATH));
        mapManager.setLevel(LoadSave.STAGE_ONE_PATH);

        try {
            backgroundImage = ImageIO.read(new File("src/main/resources/background/background.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        totalTileWidth = mapManager.getLevel().getLevelData()[0].length;
        maxTileWidth = totalTileWidth  - GamePlayPanel.MAX_SCREEN_COLUMN;
        maxOffsetWidth = maxTileWidth * GamePlayPanel.TILE_SIZE;
    }

    public void checkCloseToBorder(){
        int playerXPos = (int) player.getHitBox().x;
        int diff = playerXPos - xLevelOffset;
        if(diff > rightBorder){
            xLevelOffset += diff - rightBorder;
        }
        else if (diff < leftBorder){
            xLevelOffset += diff - leftBorder;
        }
        if(xLevelOffset > maxOffsetWidth){
            xLevelOffset = maxOffsetWidth;
        }
        else if(xLevelOffset < 0){
            xLevelOffset = 0;
        }
    }
    @Override
    public void update() {
        mapManager.update();
        player.update();
        checkCloseToBorder();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImage,0,0,GamePlayPanel.SCREEN_WIDTH,GamePlayPanel.SCREEN_HEIGHT,null);
        mapManager.draw(g, xLevelOffset);
        player.paint(g,xLevelOffset);
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
        if(eventCode == KeyEvent.VK_F5){
            gamePlayPanel.setPlaying(new Playing(gamePlayPanel));
//            gamePlayPanel.isNext = true;
        }
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

    public int getxLevelOffset() {
        return xLevelOffset;
    }

    public String getOffsetDebuggingString(){
        return "xOffset=" + xLevelOffset;
    }
}
