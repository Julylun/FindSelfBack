package com.findselfback.View;

import com.findselfback.Control.KeyHandle;
import com.findselfback.Model.Player;
import com.findselfback.Model.PrintColor;

import javax.swing.*;
import java.awt.*;
import java.security.Key;

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
public class GamePlayPanel extends JPanel implements Runnable{
    private final GameFrame thisGameFrame;

    //originalTileSize is a default variable of "game's pixel" (block)
    private final int ORIGINAL_TILE_SIZE = 128;
    private final double SCALE = 0.5;

    //the tile size after scaling
    public final int TILE_SIZE = (int)(ORIGINAL_TILE_SIZE * SCALE); //128*3 = 384px ~ 1 block pixel of game;
    public final int MAX_SCREEN_COLUMN =  16;
    public final int MAX_SCREEN_ROW =  9;
    public final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COLUMN;
    public final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;
    public final int FPS = 60;

    private KeyHandle keyHandle = new KeyHandle();
    private Player player = new Player(this,"src/main/resources/PlayerSheet.png", keyHandle);
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

        gameFrame.addKeyListener(keyHandle);

        startGameThread();
    }
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {
        PrintColor.debug(PrintColor.YELLOW_UNDERLINED,"GamePlayPanel","run","Game is running...");
        double drawInterval = 1000000000/FPS; //Average time of a frame in theory
        double nextDrawTime = System.nanoTime() + drawInterval; //The next time we wil draw a frame base on theory
        while (true){
            double firstTime = System.nanoTime();
            update(); //update
            repaint(); //paint


            try{

                //Explain how does this loop work?
                //Following computer's theory, average drawing time of a frame is equal 1000000000(nano seconds)/FPS
                //remainingTime is a value that indicates how distance time from present to the theory time is.
                //If remainingTime > 0, that means the computer completely process data before the deadline time
                //(that means the computer is strong so that has no lag here and already to draw the frame) but
                //following the theory, the frame has to be drawn at nextDrawTime, so we have to delay a time is
                // a distance from present to nextDrawTime (delay the remainingTime);
                // But else if remainingTime < 0, that means the computer is so weak and that haven't process data
                //yet baut following the theory it has to draw. so we will not delay and the screen will be lagged
                double remainingTime = nextDrawTime - System.nanoTime();
                if(remainingTime < 0){
                    remainingTime = 0;
                }
                Thread.sleep((long) (remainingTime/1000000));
                nextDrawTime += drawInterval;
                PrintColor.println(PrintColor.BLUE_BOLD, "FPS: " + fpsCounter.fps());
                fpsCounter.interrupt();
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update(){
        player.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        player.paint(g);


    }
}
