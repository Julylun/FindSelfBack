package com.findselfback.GameState;

import com.findselfback.Control.Statemethod;
import com.findselfback.Entities.Player;
import com.findselfback.Entities.Rain;
import com.findselfback.Level.LoadSave;
import com.findselfback.Level.MapManager;
import com.findselfback.Utilz.AudioPlayer;
import com.findselfback.Utilz.MP3Player;
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
    private Rain[] rainList;
    private MapManager mapManager;
    private AudioPlayer audioPlayer;
    public boolean upPressed = false, rightPressed = false, downPressed = false, leftPressed = false;
    public boolean isNoPressed = true;
    public int lastPressed;
    private int xLevelOffset;
    private int leftBorder = (int)(0.2 * GamePlayPanel.SCREEN_WIDTH);
    private int rightBorder = (int)(0.8 * GamePlayPanel.SCREEN_WIDTH);
    private int totalTileWidth;
    private int maxTileWidth;
    private int maxOffsetWidth;
    private BufferedImage backgroundImage,
            backgroundLayer2,
            backgroundLayer3,
            backgroundLayer4,
            backgroundLayer5,
            backgroundLayer6,
            backgroundLayer7,
            backgroundLayer8,
            backgroundLayer9;


    public Playing(GamePlayPanel gamePlayPanel) {
        super(gamePlayPanel);
        init();
        audioPlayer.getEnvironmentAudioList()[0].setLoop(true);
        audioPlayer.getEnvironmentAudioList()[0].play();
    }

    public void init(){
        player = new Player(this,"src/main/resources/assets/PlayerSheet2.png", gamePlayPanel.getInputHandle());
        rainList = new Rain[100];
        for(int i = 0; i < rainList.length; i++){
            rainList[i] = new Rain(this);
        }

        audioPlayer = new AudioPlayer();

        mapManager = new MapManager(gamePlayPanel);
        mapManager.autoGetSprite(LoadSave.getSpriteAtlas(LoadSave.MAP_ATLAS_PATH));
        mapManager.setLevel(LoadSave.STAGE_ONE_PATH);

        try {
            backgroundImage = ImageIO.read(new File("src/main/resources/background/background.png"));
            backgroundLayer2 = ImageIO.read(new File("src/main/resources/background/backgroundLayer2.png"));
            backgroundLayer3 = ImageIO.read(new File("src/main/resources/background/backgroundLayer3.png"));
            backgroundLayer4 = ImageIO.read(new File("src/main/resources/background/backgroundLayer4.png"));
            backgroundLayer5 = ImageIO.read(new File("src/main/resources/background/backgroundLayer5.png"));
            backgroundLayer6 = ImageIO.read(new File("src/main/resources/background/backgroundLayer6.png"));
            backgroundLayer7 = ImageIO.read(new File("src/main/resources/background/backgroundLayer7.png"));

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

    private void backgroundDraw(Graphics g){
            g.drawImage(backgroundImage,0,0,GamePlayPanel.SCREEN_WIDTH,GamePlayPanel.SCREEN_HEIGHT,null);

            g.drawImage(backgroundLayer2,(int)(xLevelOffset*0.03/GamePlayPanel.SCREEN_WIDTH) *GamePlayPanel.SCREEN_WIDTH -(int)(xLevelOffset*0.03),GamePlayPanel.SCREEN_HEIGHT-(int)(backgroundLayer2.getHeight()*GamePlayPanel.SCALE),(int)(backgroundLayer2.getWidth()*GamePlayPanel.SCALE), (int)(backgroundLayer2.getHeight()*GamePlayPanel.SCALE),null);
            g.drawImage(backgroundLayer2,(int)(xLevelOffset*0.03/GamePlayPanel.SCREEN_WIDTH + 1)*GamePlayPanel.SCREEN_WIDTH -(int)(xLevelOffset*0.03),GamePlayPanel.SCREEN_HEIGHT-(int)(backgroundLayer2.getHeight()*GamePlayPanel.SCALE),(int)(backgroundLayer2.getWidth()*GamePlayPanel.SCALE), (int)(backgroundLayer2.getHeight()*GamePlayPanel.SCALE),null);

            g.drawImage(backgroundLayer3,(int)(xLevelOffset*0.05/GamePlayPanel.SCREEN_WIDTH) *GamePlayPanel.SCREEN_WIDTH -(int)(xLevelOffset*0.05),GamePlayPanel.SCREEN_HEIGHT-(int)(backgroundLayer3.getHeight()*GamePlayPanel.SCALE),(int)(backgroundLayer3.getWidth()*GamePlayPanel.SCALE), (int)(backgroundLayer3.getHeight()*GamePlayPanel.SCALE),null);
            g.drawImage(backgroundLayer3,(int)(xLevelOffset*0.05/GamePlayPanel.SCREEN_WIDTH + 1) *GamePlayPanel.SCREEN_WIDTH -(int)(xLevelOffset*0.05),GamePlayPanel.SCREEN_HEIGHT-(int)(backgroundLayer3.getHeight()*GamePlayPanel.SCALE),(int)(backgroundLayer3.getWidth()*GamePlayPanel.SCALE), (int)(backgroundLayer3.getHeight()*GamePlayPanel.SCALE),null);

            g.drawImage(backgroundLayer4,(int)(xLevelOffset*0.08/GamePlayPanel.SCREEN_WIDTH) *GamePlayPanel.SCREEN_WIDTH  -(int)(xLevelOffset*0.08)-30,GamePlayPanel.SCREEN_HEIGHT-(int)(backgroundLayer4.getHeight()*GamePlayPanel.SCALE),(int)(backgroundLayer4.getWidth()*GamePlayPanel.SCALE), (int)(backgroundLayer4.getHeight()*GamePlayPanel.SCALE),null);
            g.drawImage(backgroundLayer4,(int)(xLevelOffset*0.08/GamePlayPanel.SCREEN_WIDTH + 1) *GamePlayPanel.SCREEN_WIDTH  -(int)(xLevelOffset*0.08)-30,GamePlayPanel.SCREEN_HEIGHT-(int)(backgroundLayer4.getHeight()*GamePlayPanel.SCALE),(int)(backgroundLayer4.getWidth()*GamePlayPanel.SCALE), (int)(backgroundLayer4.getHeight()*GamePlayPanel.SCALE),null);

            g.drawImage(backgroundLayer5,(int)(xLevelOffset*0.1/GamePlayPanel.SCREEN_WIDTH)*GamePlayPanel.SCREEN_WIDTH -(int)(xLevelOffset*0.10),GamePlayPanel.SCREEN_HEIGHT-(int)(backgroundLayer5.getHeight()*GamePlayPanel.SCALE),(int)(backgroundLayer5.getWidth()*GamePlayPanel.SCALE), (int)(backgroundLayer5.getHeight()*GamePlayPanel.SCALE),null);
            g.drawImage(backgroundLayer5,(int)(xLevelOffset*0.1/GamePlayPanel.SCREEN_WIDTH + 1)*GamePlayPanel.SCREEN_WIDTH -(int)(xLevelOffset*0.10),GamePlayPanel.SCREEN_HEIGHT-(int)(backgroundLayer5.getHeight()*GamePlayPanel.SCALE),(int)(backgroundLayer5.getWidth()*GamePlayPanel.SCALE), (int)(backgroundLayer5.getHeight()*GamePlayPanel.SCALE),null);

            g.drawImage(backgroundLayer6,(int)(xLevelOffset*0.15/GamePlayPanel.SCREEN_WIDTH)*GamePlayPanel.SCREEN_WIDTH -(int)(xLevelOffset*0.15)-30,GamePlayPanel.SCREEN_HEIGHT-(int)(backgroundLayer6.getHeight()*GamePlayPanel.SCALE-50),(int)(backgroundLayer6.getWidth()*GamePlayPanel.SCALE), (int)(backgroundLayer6.getHeight()*GamePlayPanel.SCALE),null);
            g.drawImage(backgroundLayer6,(int)(xLevelOffset*0.15/GamePlayPanel.SCREEN_WIDTH + 1)*GamePlayPanel.SCREEN_WIDTH -(int)(xLevelOffset*0.15)-30,GamePlayPanel.SCREEN_HEIGHT-(int)(backgroundLayer6.getHeight()*GamePlayPanel.SCALE-50),(int)(backgroundLayer6.getWidth()*GamePlayPanel.SCALE), (int)(backgroundLayer6.getHeight()*GamePlayPanel.SCALE),null);

            g.drawImage(backgroundLayer7,(int)(xLevelOffset*0.25/GamePlayPanel.SCREEN_WIDTH) *GamePlayPanel.SCREEN_WIDTH -(int)(xLevelOffset*0.25),GamePlayPanel.SCREEN_HEIGHT-(int)(backgroundLayer7.getHeight()*GamePlayPanel.SCALE),(int)(backgroundLayer7.getWidth()*GamePlayPanel.SCALE), (int)(backgroundLayer7.getHeight()*GamePlayPanel.SCALE),null);
            g.drawImage(backgroundLayer7,(int)(xLevelOffset*0.25/GamePlayPanel.SCREEN_WIDTH + 1) *GamePlayPanel.SCREEN_WIDTH -(int)(xLevelOffset*0.25),GamePlayPanel.SCREEN_HEIGHT-(int)(backgroundLayer7.getHeight()*GamePlayPanel.SCALE),(int)(backgroundLayer7.getWidth()*GamePlayPanel.SCALE), (int)(backgroundLayer7.getHeight()*GamePlayPanel.SCALE),null);


    }
    @Override
    public void update() {
        mapManager.update();
        player.update();
        for(Rain rain: rainList){
            rain.update();
        }
        checkCloseToBorder();
    }

    @Override
    public void draw(Graphics g) {
        backgroundDraw(g);
        mapManager.draw(g, xLevelOffset);
        player.paint(g,xLevelOffset);
        for(Rain rain: rainList){
            rain.paint(g,xLevelOffset);
        }
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
        if(eventCode == KeyEvent.VK_F12){
            if(!gamePlayPanel.isMapEditingMode())
            {
                gamePlayPanel.editingModeInit();
            }
            gamePlayPanel.setMapEditingMode();
        }
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
