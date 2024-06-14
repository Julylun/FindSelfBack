package com.findselfback.GameState;

import com.findselfback.Control.Statemethod;
import com.findselfback.Entities.*;
import com.findselfback.Level.EventManager;
import com.findselfback.Level.LoadSave;
import com.findselfback.Level.MapManager;
import com.findselfback.Utilz.AudioPlayer;
import com.findselfback.Utilz.Constant;
import com.findselfback.Utilz.Conversation;
import com.findselfback.View.GamePlayPanel;
import lombok.Getter;
import lombok.Setter;
import com.findselfback.Entities.NPC;
import org.hibernate.boot.cfgxml.internal.CfgXmlAccessServiceInitiator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;


public class Playing extends State implements Statemethod {
    private Player player; //<--
    private Rabbit[] rabbitList;
    private SmokeArea[] smokeFxList;
    private FX[] FXList;
    private NPC[] NPCList;
    private Snow[] snowList;
    public float windSpeed; // > 0 thì đi về bên phải < 0 thì đi bén trái = 0 gió đứng im



    public int delayWindTime = 400;
    public int currentWindTick = 0;
    private MapManager mapManager;
    @Getter
    private AudioPlayer audioPlayer;
    @Getter
    private Subtitle subtitle;
    @Getter
    private EventManager eventManager;
    @Getter
    @Setter
    public boolean EButton = false, upPressed = false, rightPressed = false, downPressed = false, leftPressed = false, shiftPressed = false;
    public boolean isNoPressed = true;
    public int lastPressed;
    private int xLevelOffset;
    private int leftBorder = (int)(0.3 * GamePlayPanel.SCREEN_WIDTH);
    private int rightBorder = (int)(0.5 * GamePlayPanel.SCREEN_WIDTH);
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

    }

    public void init(){
        mapManager = new MapManager(gamePlayPanel);
        mapManager.autoGetSprite(LoadSave.getSpriteAtlas(LoadSave.MAP_ATLAS_PATH));
        mapManager.setLevel(LoadSave.STAGE_ONE_PATH);
        subtitle = new Subtitle(this);
        eventManager = new EventManager(this);

        windSpeed = (float) ThreadLocalRandom.current().nextDouble(-1,1);

        //Character initialize
        player = new Player(this,"Resources/assets/PlayerSheet2.png", gamePlayPanel.getInputHandle());
        NPCInit();
        FXInit();
        snowList = new Snow[25];
        rabbitList = new Rabbit[10];
        for(int i = 0; i < snowList.length; i++){
            snowList[i] = new Snow(this);
        }
        for(int i = 0; i < rabbitList.length; i++){
            rabbitList[i] = new Rabbit(this, "Resources/assets/RabbitSpriteSheet.png");
        }


        try {
            backgroundImage = ImageIO.read(new File("Resources/background/background.png"));
            backgroundLayer2 = ImageIO.read(new File("Resources/background/backgroundLayer2.png"));
            backgroundLayer3 = ImageIO.read(new File("Resources/background/backgroundLayer3.png"));
            backgroundLayer4 = ImageIO.read(new File("Resources/background/backgroundLayer4.png"));
            backgroundLayer5 = ImageIO.read(new File("Resources/background/backgroundLayer5.png"));
            backgroundLayer6 = ImageIO.read(new File("Resources/background/backgroundLayer6.png"));
            backgroundLayer7 = ImageIO.read(new File("Resources/background/backgroundLayer7.png"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        totalTileWidth = mapManager.getLevel().getLevelData()[0].length;
        maxTileWidth = totalTileWidth  - GamePlayPanel.MAX_SCREEN_COLUMN;
        maxOffsetWidth = maxTileWidth * GamePlayPanel.TILE_SIZE;

        audioPlayer = new AudioPlayer();
        audioPlayer.getEnvironmentAudioList()[0].setLoop(true);

        //TEST
        subtitle.setCurrentConversation(Conversation.Begin.FIRST_TALK);
        subtitle.setDisplay(true);
//        subtitle.nextSubtitle();

    }

    private void FXInit(){
        FXList = new FX[3];
        FXList[0] = new FX(this,Constant.AssetPath.FX.LIGHT,320,397,32,32,8,4);
        FXList[1] = new FX(this,Constant.AssetPath.FX.LIGHT,350,397,32,32,8,4);
        FXList[2] = new FX(this,Constant.AssetPath.FX.TRAFFIC_POLICE_LIGHT,1603, 413,32,32,8,4);

        smokeFxList = new SmokeArea[1];
        smokeFxList[0] = new SmokeArea(this, 10, 1956,450,20,20);
    }
    private void NPCInit(){
        NPCList = new NPC[1];
        NPCList[NPC.TRAFFIC_POLICE] = new NPC(this,"Resources/assets/NPC/traffic_police.png",
                GamePlayPanel.ORIGINAL_TILE_SIZE,GamePlayPanel.ORIGINAL_TILE_SIZE,NPC.TRAFFIC_POLICE);

        NPCList[NPC.TRAFFIC_POLICE].getSpriteSheet().createSprite(
                Constant.Animation.IDLE,
                0,0,GamePlayPanel.ORIGINAL_TILE_SIZE,GamePlayPanel.ORIGINAL_TILE_SIZE,8
        );
        NPCList[NPC.TRAFFIC_POLICE].getSpriteSheet().setCurrentSprite(Constant.Animation.IDLE);
        NPCList[NPC.TRAFFIC_POLICE].getSpriteSheet().setDelayTime(10);
        NPCList[NPC.TRAFFIC_POLICE].configure(26*GamePlayPanel.TILE_SIZE,
                7*GamePlayPanel.TILE_SIZE,
                GamePlayPanel.TILE_SIZE,
                GamePlayPanel.TILE_SIZE
        );
        NPCList[NPC.TRAFFIC_POLICE].setEventBox(25*GamePlayPanel.TILE_SIZE,6*GamePlayPanel.TILE_SIZE,
                3*GamePlayPanel.TILE_SIZE,3*GamePlayPanel.TILE_SIZE);

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
        if(currentWindTick < delayWindTime){
            currentWindTick+=1;
        } else {
            currentWindTick = 0;
            delayWindTime = ThreadLocalRandom.current().nextInt(100,1000);
            windSpeed = (float) ThreadLocalRandom.current().nextDouble(-1,1);
        }
        mapManager.update();
        for(int i = 0; i < rabbitList.length; i++){
            rabbitList[i].update();
        }
        for(int i = 0; i < smokeFxList.length; i++){
            smokeFxList[i].update();
        }
        player.update();
        for(Snow snow: snowList){
            snow.update();
        }
        for(int index = 0; index < NPCList.length; index++){
            NPCList[index].update();
        }
        eventManager.update();
        checkCloseToBorder();
    }

    @Override
    public void draw(Graphics g) {
        //Draw background
        backgroundDraw(g);

        //Draw tile map
        mapManager.draw(g, xLevelOffset);
        for(int i = 0; i < rabbitList.length; i++){
            rabbitList[i].paint(g,xLevelOffset);
        }

        for(int index = 0; index < NPCList.length; index++){
            NPCList[index].paint(g,xLevelOffset);
        }
        for(int i = 0; i < smokeFxList.length; i++){
            smokeFxList[i].paint(g,xLevelOffset);
        }
        //Draw player
        player.paint(g,xLevelOffset);
        //Draw snow
        for(int i = 0; i < FXList.length; i++){
            FXList[i].paint(g,xLevelOffset);
        }
        for(Snow snow: snowList){
            snow.paint(g,xLevelOffset);
        }

        //Draw subtitle and task text
        subtitle.paint(g,xLevelOffset);
        eventManager.paint(g,xLevelOffset);
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
        if(eventCode == KeyEvent.VK_SHIFT){
            shiftPressed = true;
        }
        if(eventCode == KeyEvent.VK_E){
            EButton = true;
            System.out.println("EEEwdd");
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
        if(eventCode == KeyEvent.VK_SHIFT){
            shiftPressed = false;
        }
        if(eventCode == KeyEvent.VK_E){
            EButton = false;
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
}
