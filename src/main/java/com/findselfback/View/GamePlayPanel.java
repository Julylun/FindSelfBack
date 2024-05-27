package com.findselfback.View;

import com.findselfback.Control.InputHandle;
import com.findselfback.GameState.GameState;
import com.findselfback.GameState.MenuState;
import com.findselfback.GameState.Playing;
import com.findselfback.Level.LoadSave;
import com.findselfback.Level.MapManager;
import com.findselfback.Utilz.Constant;
import com.findselfback.Utilz.PrintColor;
import com.thoughtworks.qdox.model.expression.Or;
import lombok.Data;
import lombok.Getter;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;
class FPSCounter extends Thread{
    private long lastTime;
    private double fps; //could be int or long for integer values


    public void run(){
        while (true){//lazy me, add a condition for an finishable thread
            lastTime = System.nanoTime();
            try{
                Thread.sleep(1000); // longer than one frame
            }
            catch (InterruptedException e){

            }
            fps = 1000000000.0 / (System.nanoTime() - lastTime); //one second(nano) divided by amount of time it takes for one frame to finish
            lastTime = System.nanoTime();
        }
    }
    public double fps(){
        return fps;
    }
}
@Data
public class GamePlayPanel extends JPanel implements Runnable{
    private final GameFrame thisGameFrame;
    private double currentFPS = 0, currentUPS = 0;
    private boolean isDebugging = true;

    private boolean mapEditingMode = false;
    public int currentTileEditingMode = 0;
    public boolean isPressing = false;
    public boolean isMapEnable = false;
    public boolean isChooseTileEnable = false;
    public boolean isSaving = false;
    public BufferedImage editingMap;
    public BufferedImage tileMap;

    private BufferedImage[] tileMapForMapEditing;

    //originalTileSize is a default variable of "game's pixel" (block)
    public static final int ORIGINAL_TILE_SIZE = 32;
    public static final double SCALE = 2; //scaling value


    //the tile size after scaling
    public static final int TILE_SIZE = (int)(ORIGINAL_TILE_SIZE * SCALE); //128*3 = 384px ~ 1 block pixel of game;
    public static final int MAX_SCREEN_COLUMN =  16;
    public static final int MAX_SCREEN_ROW =  9;
    public static final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COLUMN;
    public static final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;
    public static final int FPS = 60;
    public static final int UPS = 200;

    public boolean isNext = false;

    private InputHandle inputHandle = new InputHandle(this);

    private Playing playing = new Playing(this);
    private MenuState menuState = new MenuState(this);

    private Thread gameThread;
    private FPSCounter fpsCounter;

    public GamePlayPanel(GameFrame gameFrame){
        //A reference points to GameView, this will be used for many future task
        //Eg: if you want to change size of windows, you have to call this reference like below
        PrintColor.debug(PrintColor.CYAN_BRIGHT, "GameViewPanel","Constuctor",
                "Screen: " + SCREEN_WIDTH + "x" + SCREEN_HEIGHT);
        thisGameFrame = gameFrame;
        setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        fpsCounter = new FPSCounter();
        fpsCounter.start();

        addMouseMotionListener(inputHandle);
        addMouseListener(inputHandle);
        addMouseWheelListener(inputHandle);
        gameFrame.addKeyListener(inputHandle);
        init();
        startGameThread();
    }
    public void init(){

    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {
        PrintColor.debug(PrintColor.YELLOW_UNDERLINED,"GamePlayPanel","run","Game is running...");
        double drawInterval = 1000000000/FPS; //Average time of a frame in theory
        double updateInterval = 1000000000/UPS;

        double deltaFrame = 0;
        double deltaUpdate  = 0;
        double lastCheck = System.currentTimeMillis();
        double previousTime = System.nanoTime();
        double frames = 0, updates = 0;

        while (true){
            double currentTime = System.nanoTime();

            deltaUpdate += (currentTime - previousTime) / updateInterval;
            deltaFrame += (currentTime - previousTime) / drawInterval;
            previousTime = currentTime;


            if(deltaUpdate >= 1) {
                update();
                updates++;
                deltaUpdate--;
            }
            if(isNext){
                isNext = false;
                continue;
            }
            if(deltaFrame >= 1) {
                repaint();
                frames++;
                deltaFrame--;
//                fpsCounter.interrupt();
            }

            if(System.currentTimeMillis() - lastCheck >= 1000){
                lastCheck +=1000;
//                PrintColor.debug(PrintColor.CYAN_BRIGHT,"GamePlayPanel","run","FPS: " + frames + " | UPS: " + updates);
                currentFPS = frames;
                currentUPS = updates;
                frames = 0;
                updates = 0;
            }



                //Explain how does this loop work?
                //Following computer's theory, average drawing time of a frame is equal 1000000000(nano seconds)/FPS
                //remainingTime is a value that indicates how distance time from present to the theory time is.
                //If remainingTime > 0, that means the computer completely process data before the deadline time
                //(that means the computer is strong so that has no lag here and already to draw the frame) but
                //following the theory, the frame has to be drawn at nextDrawTime, so we have to delay a time is
                // a distance from present to nextDrawTime (delay the remainingTime);
                // But else if remainingTime < 0, that means the computer is so weak and that haven't process data
                //yet baut following the theory it has to draw. so we will not delay and the screen will be lagged

    }
    }

    public void drawDebug(Graphics g){
        if(isDebugging){

            g.setFont(Constant.DefaultFont.DEFAULT);
            g.setColor(Color.YELLOW);
            g.drawString("FPS: "+ currentFPS + " - UPS: " + currentUPS, 0, 20);
            g.drawString((inputHandle.getLastKeyEvent() == null) ? "Key: ": "Key: " + inputHandle.getLastKeyEvent().paramString(), 0, 40);
            g.drawString("Player: " + playing.getPlayer().getStringLocation(), 0, 60);
            g.drawString("Sreen size: " + thisGameFrame.getSize(), 0, 80);
            g.drawString((inputHandle.getLastMouseEvent() == null) ? "Mouse: null": "Mouse: " + inputHandle.getLastMouseEvent().paramString(), 0, 100);
            g.drawString(playing.getOffsetDebuggingString(),0,120);
            g.drawString("Map editing mode: " + isMapEditingMode() + "|| F12: on/off Map editor || F11: on/off Editing map || F6: on/off tile chooser || F2: save map || Right click: move character to mouse || Left click: draw tile map",0,140);

            g.setColor(Color.BLACK);
        }
        if(isMapEditingMode()){
            g.setColor(Color.YELLOW);
            g.drawString("current Tile: " + currentTileEditingMode,0,160);
            g.setColor(Color.BLACK);
            Point mousePoint = getMousePosition();
            MouseEvent mouseEvent = inputHandle.getLastMouseEvent();
            if(mousePoint != null){
                int mouseCol = (mousePoint.x)/TILE_SIZE;
                int mouseRow = mousePoint.y/TILE_SIZE;
                int realMouseCol = (mousePoint.x + getPlaying().getxLevelOffset())/TILE_SIZE;
                g.setColor(Color.BLUE);
                if(!isChooseTileEnable){
                    g.drawRect(mouseCol*TILE_SIZE - (playing.getxLevelOffset()%TILE_SIZE) -1, mouseRow*TILE_SIZE-1,TILE_SIZE+2,TILE_SIZE+2);
                    if(currentTileEditingMode!=-1){
                        g.drawImage(tileMapForMapEditing[currentTileEditingMode],
                                mouseCol*TILE_SIZE - (playing.getxLevelOffset()%TILE_SIZE),mouseRow*TILE_SIZE,TILE_SIZE,TILE_SIZE,null);
                    }
                }

                if(isMapEnable){
                    g.drawImage(editingMap,0,0,editingMap.getWidth()*10,editingMap.getHeight()*10,null);
                }
                if(isPressing && !isChooseTileEnable){
                    MouseEvent clickEnvent = inputHandle.getLastClickEvent();
                    if(clickEnvent.getButton() == 3){
                        playing.getPlayer().setX(clickEnvent.getX());
                        playing.getPlayer().setY(clickEnvent.getY());
                    } else
                    if(currentTileEditingMode != -1){
                        editingMap.setRGB(realMouseCol,mouseRow,new Color(currentTileEditingMode,0,0).getRGB());
                        playing.getMapManager().getLevel().getLevelData()[mouseRow][realMouseCol] = currentTileEditingMode;
                    }

                }
                if(isChooseTileEnable){
                    g.drawImage(tileMap,0,0,tileMap.getWidth(),tileMap.getHeight(),null);
                    if(mousePoint.x <= tileMap.getWidth() && mousePoint.y  <= tileMap.getHeight()){
                        g.setColor(Color.white);
                        g.drawRect(mousePoint.x/ORIGINAL_TILE_SIZE*ORIGINAL_TILE_SIZE,mousePoint.y/ORIGINAL_TILE_SIZE*ORIGINAL_TILE_SIZE, ORIGINAL_TILE_SIZE,ORIGINAL_TILE_SIZE);
                        g.setColor(Color.black);
                        if(isPressing){
                            currentTileEditingMode = mousePoint.y/ORIGINAL_TILE_SIZE* (tileMap.getWidth()/ORIGINAL_TILE_SIZE) + mousePoint.x/ORIGINAL_TILE_SIZE;
                        }
                    }
                }
                if(isSaving){
                    try {
                        ImageIO.write(editingMap,"png",new File("src/main/resources/map/state_1.png"));
                        System.out.println("Saved map at src/main/resources/map/state_1.png");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    isSaving = false;

                }
            }

        }
    }
    public void update(){
        switch (GameState.state){
            case PLAYING:
                playing.update();
                break;
            case MENU:
                menuState.update();
                break;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        switch (GameState.state){
            case PLAYING:
                playing.draw(g);
                break;
            case MENU:
                menuState.draw(g);
                break;
        }
        drawDebug(g);



    }

    public boolean isNext() {
        return isNext;
    }

    public void setNext(boolean next) {
        isNext = next;
    }

    public void setMapEditingMode() {
        this.mapEditingMode = !this.mapEditingMode;
    }

    public void editingModeInit(){
        tileMap = LoadSave.getSpriteAtlas("src/main/resources/assets/terrain_sprite_sheet_map.png");
        tileMapForMapEditing = MapManager._autoGetSprite(tileMap,ORIGINAL_TILE_SIZE,ORIGINAL_TILE_SIZE);
        editingMap = LoadSave.getSpriteAtlas("src/main/resources/map/state_1.png");
        playing.getPlayer().setGravity(0.002f);
        playing.getPlayer().setSpeed(4);
//        playing.getPlayer().setFallingSpeedAfterCollision(0);
//        playing.getPlayer().setJumpSpeed(-3);
    }

    public void deleteAllMap(){
        for(int i = 0; i < editingMap.getHeight(); i++){
            for(int j = 0; j < editingMap.getWidth(); j++){
                editingMap.setRGB(j,i,new Color(11,0,0).getRGB());
                playing.getMapManager().getLevel().getLevelData()[i][j] = 11;
            }
        }

    }

}
