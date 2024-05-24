package com.findselfback.View;

import com.findselfback.Control.InputHandle;
import com.findselfback.GameState.GameState;
import com.findselfback.GameState.MenuState;
import com.findselfback.GameState.Playing;
import com.findselfback.Utilz.Constant;
import com.findselfback.Utilz.PrintColor;
import lombok.Data;

import javax.swing.*;
import java.awt.*;

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
            g.drawString("Sreen size: " + thisGameFrame.getSize(), 0, 80);
            g.drawString((inputHandle.getLastKeyEvent() == null) ? "Key: ": "Key: " + inputHandle.getLastKeyEvent().paramString(), 0, 40);
            g.drawString((inputHandle.getLastMouseEvent() == null) ? "Mouse: null": "Mouse: " + inputHandle.getLastMouseEvent().paramString(), 0, 100);
            g.drawString("Player: " + playing.getPlayer().getStringLocation(), 0, 60);
            g.setColor(Color.BLACK);
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
}
